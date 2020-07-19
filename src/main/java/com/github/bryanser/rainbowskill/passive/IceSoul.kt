package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Passive
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

/**
 * 在生命值低于5%的时候，生成一个冰球把自己困在里面，持续3s。
 */
object IceSoul : Passive("冰魂") {
    const val KEY = "冰魂"
    val time = ConfigEntry("Time", 3000.0)

    val activing = hashSetOf<UUID>()

    override fun init() {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onDamage(evt: EntityDamageByEntityEvent) {
        val d = evt.damager as? Player ?: return
        val h = d.health - evt.finalDamage
        val max = d.getAttribute(Attribute.GENERIC_MAX_HEALTH).value
        if (h / max <= 0.05) {
            for (z in 0 until 1) {
                for (i in -1 until 1) {
                    for (j in -1 until 1) {
                        if (i != 0 && j != 0 && z != 0) {
                            val currLoc = d.location.add(i.toDouble(), z.toDouble(), j.toDouble())
                            currLoc.block.type = Material.ICE
                            currLoc.block.state.update()
                        }
                    }
                }
            }
        }
    }

    override fun startPassive(p: Player) {
        activing.add(p.uniqueId)
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }

}