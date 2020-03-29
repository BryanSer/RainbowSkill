package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.*
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

    }

    override fun startPassive(p: Player) {
        activing.add(p.uniqueId)
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }

}