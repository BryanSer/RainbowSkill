package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

fun MutableList<AttributeModifier>.removeAttribute(key: String) {
    val it = this.iterator()
    while (it.hasNext()) {
        val n = it.next()
        if (n.key == key) {
            it.remove()
        }
    }
}

object Furious : Passive("狂怒") {
    const val KEY = "狂怒"

    val activing = hashMapOf<UUID, Int>()

    val crit = ConfigEntry("Crit", 0.5)

    val criting = hashSetOf<UUID>()

    override fun init() {
    }

    override fun startPassive(p: Player) {
        activing[p.uniqueId] = 0
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onAttack(evt: EntityDamageByEntityEvent) {
        val d = evt.damager as? Player ?: return
        var t = (activing[d.uniqueId] ?: return) + 1
        if (t >= 3) {
            t = 0
            criting += d.uniqueId
            AttributeManager[d]["CritChance"]?.also {
                it.removeAttribute(KEY)
                it.add(AttributeModifier(ValueType.PLUS, crit(CastData(d, 1)).toDouble(), KEY))
            }
        }
        activing[d.uniqueId] = t
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onAttackEnd(evt: EntityDamageByEntityEvent) {
        val d = evt.damager as? Player ?: return
        if (criting.remove(d.uniqueId)) {
            AttributeManager[d]["CritChance"]?.also {
                it.removeAttribute(KEY)
            }
        }
    }

    override fun stopPassive(p: Player) {
        val attr = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
        attr.modifiers.iterator().run {
            while (hasNext()) {
                val n = next()
                if (n.name == Alacrity.KEY) {
                    remove()
                }
            }
        }
        activing.remove(p.uniqueId)
    }

}