package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Passive
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

/**
 * 每当受到伤害，有2%的几率直接闪避，包括技能。
 */
object DeathProof : Passive("金刚不坏") {
    const val KEY = "金刚不坏"
    val activing = hashSetOf<UUID>()

    val range = ConfigEntry("range", 3.0)
    val time = ConfigEntry("time", 2.0)

    override fun init() {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onDamage(evt: EntityDamageByEntityEvent) {
        if (Math.random() <= 0.02) {
            evt.damage = 0.0
        }
    }

    override fun startPassive(p: Player) {
        activing.add(p.uniqueId)
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }

}