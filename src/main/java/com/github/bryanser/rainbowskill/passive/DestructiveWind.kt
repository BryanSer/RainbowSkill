package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

/**
 * 每次普攻都获得5%的暴击几率
 */
object DestructiveWind : Passive("箭风") {
    const val KEY = "箭风"
    val criticalHitRate = ConfigEntry("CriticalHitRate", 0.05)

    val activing = hashMapOf<UUID, Long>()
    override fun init() {
    }

    val criting = hashSetOf<UUID>()

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onAttack(evt: EntityDamageByEntityEvent) {
        val d = evt.damager as? Player ?: return
        if (!activing.contains(d.uniqueId)) return
        criting += d.uniqueId
        AttributeManager[d]["CriticalHitRate"]?.also {
            it.removeAttribute(KEY)
            it.add(AttributeModifier(ValueType.PLUS, criticalHitRate(CastData(d, 1)).toDouble(), KEY))
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onAttackEnd(evt: EntityDamageByEntityEvent) {
        val d = evt.damager as? Player ?: return
        if (criting.remove(d.uniqueId)) {
            AttributeManager[d]["CriticalHitRate"]?.also {
                it.removeAttribute(KEY)
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