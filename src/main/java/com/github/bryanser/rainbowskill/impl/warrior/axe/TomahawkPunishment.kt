package com.github.bryanser.rainbowskill.impl.warrior.axe

import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.passive.DestructiveWind
import com.github.bryanser.rainbowskill.passive.removeAttribute
import org.bukkit.Material
import org.bukkit.scheduler.BukkitRunnable


/**
 * 使用技能后的3s，增加150%的普攻，并增加20%的暴击几率，每次攻击附带方块破碎的粒子，技能有效时出现红色粒子在身边
 */
object TomahawkPunishment : Skill(
        "战斧惩戒",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("AttackIncrease", 1.5),
                ConfigEntry("CriticalHitRate", 0.2),
                ConfigEntry("DurationTime", 3.0)
        )) {
    const val KEY = "战斧惩戒"

    override fun onCast(cd: CastData): Boolean {
        val attackIncrease = getConfigEntry("AttackIncrease")(cd).toDouble()
        val criticalHitRate = getConfigEntry("CriticalHitRate")(cd).toDouble()
        val durationTime = getConfigEntry("DurationTime")(cd).toDouble()

        val p = cd.caster




        return true
    }

}