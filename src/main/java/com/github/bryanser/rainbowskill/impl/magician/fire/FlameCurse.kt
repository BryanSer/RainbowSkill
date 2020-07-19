package com.github.bryanser.rainbowskill.impl.magician.fire

import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.StatusIncrease
import com.github.bryanser.rainbowskill.passive.Uncertain.remove
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.scheduler.BukkitRunnable

/**
 * 增加10%的攻击和10%的速度，持续3s
 */
object FlameCurse : Skill(
        "烈焰魔咒",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("AttackIncrease",0.1),
                ConfigEntry("SpeedIncrease", 0.1),
                ConfigEntry("DurationTime",3.0)
        )) {
    const val KEY = "烈焰魔咒"

    override fun onCast(cd: CastData): Boolean {
        val attackIncrease = getConfigEntry("AttackIncrease")(cd).toDouble()
        val speedIncrease = getConfigEntry("SpeedIncrease")(cd).toDouble()
        val durationTime = getConfigEntry("DurationTime")(cd).toDouble()

        val p = cd.caster

        StatusIncrease.statusIncrease(KEY,"attack",cd,attackIncrease,durationTime)
        StatusIncrease.statusIncrease(KEY,"speed",cd,speedIncrease,durationTime)
        return true
    }

}