package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

object Blast : Passive("疾风") {
    const val KEY = "疾风"
    val activing = hashSetOf<UUID>()
    val critDamage = ConfigEntry("CritDamage", 0.25)
    override fun init() {
    }

    val criting = hashSetOf<UUID>()

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onAttack(evt: EntityDamageByEntityEvent) {
        val d = evt.damager as? Player ?: return
        if (!activing.contains(d.uniqueId)) return
        criting += d.uniqueId
        AttributeManager[d]["CritDamage"]?.also {
            it.removeAttribute(KEY)
            it.add(AttributeModifier(ValueType.PLUS, critDamage(CastData(d, 1)).toDouble(), KEY))
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onAttackEnd(evt: EntityDamageByEntityEvent) {
        val d = evt.damager as? Player ?: return
        if (criting.remove(d.uniqueId)) {
            AttributeManager[d]["CritDamage"]?.also {
                it.removeAttribute(KEY)
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