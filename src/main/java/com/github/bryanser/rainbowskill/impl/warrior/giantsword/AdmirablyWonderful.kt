package com.github.bryanser.rainbowskill.impl.warrior.giantsword

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material


//对前方长5宽1范围打出绿色粒子，造成伤害的同时还将击飞，并沉默敌人1s
object AdmirablyWonderful : Skill(
        "石破天惊",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()

        val enemyList = SkillUtils.rangeAttack(cd, 2.0, 3.0)
        enemyList.forEach {
            SkillUtils.damage(cd,it,dmg)
            Motion.knock(cd,it,distance)
        }

        return true
    }
}