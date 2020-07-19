package com.github.bryanser.rainbowskill.impl.assassin.dagger

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.impl.warrior.axe.TomahawkPunishment
import com.github.bryanser.rainbowskill.passive.removeAttribute
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*


/**
 * 下次普攻强化为100%暴击，并增加移动速度1s
 */
object BonePiercing : Skill("刺骨", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("SpeedTimes", 1.0)
        )) {
    val activing = hashSetOf<UUID>()
    const val KEY = "刺骨"

    override fun onCast(cd: CastData): Boolean {
        val p = cd.caster
        val speedTimes = getConfigEntry("SpeedTimes")(cd).toDouble()

        AttributeManager[p]["CriticalHitRate"]?.also {
            it.removeAttribute(TomahawkPunishment.KEY)
            it.add(AttributeModifier(ValueType.PLUS, 1.0, KEY))
        }

        SpeedManager.newData().also {
            it.modifier = 0.5
            it.timeLength = speedTimes
            SpeedManager.addEffect(cd, p, it)
        }
        return true
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onAttack(evt: EntityDamageByEntityEvent) {
        val p = evt.damager as? Player ?: return
        if (p.uniqueId in activing) {
            AttributeManager[p]["CriticalHitRate"]?.also {
                it.removeAttribute(TomahawkPunishment.KEY)
            }
            activing.remove(p.uniqueId)
        }
    }

}