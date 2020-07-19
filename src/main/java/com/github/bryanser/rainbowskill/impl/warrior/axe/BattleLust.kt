package com.github.bryanser.rainbowskill.impl.warrior.axe

import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.ImmobilizeManager
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.SkillUtils
import com.github.bryanser.rainbowskill.passive.Uncertain
import com.github.bryanser.rainbowskill.passive.Uncertain.remove
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.scheduler.BukkitRunnable

/**
 * 增加10%的攻击和20%的速度，持续5s
 */
object BattleLust : Skill(
        "战斗欲望",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("AttackIncrease",0.1),
                ConfigEntry("SpeedIncrease", 0.2),
                ConfigEntry("DurationTime",5.0)
        )) {
    const val KEY = "战斗欲望"

    override fun onCast(cd: CastData): Boolean {
        val attackIncrease = getConfigEntry("AttackIncrease")(cd).toDouble()
        val speedIncrease = getConfigEntry("SpeedIncrease")(cd).toDouble()
        val durationTime = getConfigEntry("DurationTime")(cd).toDouble()

        val p = cd.caster


        val attr = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
        attr.remove()
        attr.addModifier(AttributeModifier(KEY, speedIncrease,AttributeModifier.Operation.MULTIPLY_SCALAR_1))

        val attr2 = p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
        attr2.remove()
        attr2.addModifier(AttributeModifier(KEY, attackIncrease,AttributeModifier.Operation.MULTIPLY_SCALAR_1))

        object : BukkitRunnable() {
            override fun run() {
                p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).remove()
                p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).remove()
            }
        }.runTaskLater(Main.getPlugin(), (durationTime * 20).toLong())

        return true
    }

}