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
 *
隐身5s，普攻加15%
 */
object Ready : Skill("蓄势待发", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("AttackIncrease", 0.15),
                ConfigEntry("SpeedTimes", 1.0)
        )) {
    val activing = hashSetOf<UUID>()
    const val KEY = "蓄势待发"

    override fun onCast(cd: CastData): Boolean {
        val p = cd.caster
        val attackIncrease = getConfigEntry("AttackIncrease")(cd).toDouble()

        AttributeManager[p]["AttackIncrease"]?.also {
            it.removeAttribute(KEY)
            it.add(AttributeModifier(ValueType.PLUS, attackIncrease, KEY))
        }

        //隐身

        return true
    }


}