package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Passive
import com.github.bryanser.rainbowskill.motion.distanceSquared2
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.metadata.FixedMetadataValue
import java.util.*

object Fortitude : Passive("坚毅") {
    val KEY = "坚毅"

    val time = ConfigEntry("Time", 5000.0)
    val recover = ConfigEntry("Recover", 0.01)
    val recoverTime = ConfigEntry("RecoverTime", 2000.0)

    val activing = hashSetOf<UUID>()

    override fun init() {
        Bukkit.getScheduler().runTaskTimer(Main.Plugin, {
            for (uid in activing) {
                val p = Bukkit.getPlayer(uid) ?: continue
                val last = p.getMetadata(KEY)?.firstOrNull()?.asLong() ?: continue
                val pass = System.currentTimeMillis() - last
                val cd = CastData(p, 1)
                if (pass >= time(cd).toLong()) {
                    val max = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).value
                    val r = recover(cd).toDouble() * max
                    var h = p.health + r
                    if (h > max) {
                        h = max
                    }
                    p.health = h
                    p.removeMetadata(KEY, Main.Plugin)
                    p.setMetadata(KEY, FixedMetadataValue(Main.Plugin, last + recoverTime(cd).toLong()))
                }
            }
        }, 5, 5)
    }

    @EventHandler
    fun onMove(evt: PlayerMoveEvent) {
        val dis = evt.from.distanceSquared2(evt.to)
        if (dis > 0.00001 && activing.contains(evt.player.uniqueId)) {
            evt.player.removeMetadata(KEY, Main.Plugin)
            evt.player.setMetadata(KEY, FixedMetadataValue(Main.Plugin, System.currentTimeMillis()))
        }
    }

    override fun startPassive(p: Player) {
        activing.add(p.uniqueId)
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }


}