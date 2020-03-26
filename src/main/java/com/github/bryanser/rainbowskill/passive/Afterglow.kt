package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Passive
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

object Afterglow : Passive("残影") {
    val activing = hashMapOf<UUID, Long>()
    val health = ConfigEntry("Health", 0.3)
    val cooldown = ConfigEntry("Cooldown", 120 * 1000.0)
    val time = ConfigEntry("Time", 30.0)
    override fun init() {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDamage(evt: EntityDamageEvent) {
        val p = evt.entity as? Player ?: return
        val rem = p.health - evt.finalDamage
        val max = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).value
        val cd = CastData(p)
        if (rem / max < health(cd).toDouble()) {
            val last = activing[p.uniqueId] ?: return
            val pass = System.currentTimeMillis() - last
            if (pass > cooldown(cd).toDouble()) {
                p.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, time(cd).toInt(), 1))
                activing[p.uniqueId] = System.currentTimeMillis()
            }
        }

    }

    override fun startPassive(p: Player) {
        activing[p.uniqueId] = 0L
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }

}