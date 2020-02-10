package com.github.bryanser.rainbowskill.impl.warrior.lance

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material

//技能1：突刺
//向前冲锋5格，对冲锋路径上的敌人造成伤害并击退
object Spike : Skill("突刺", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 1.0)
        )) {

    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = getConfigEntry("Distance")(cd).toDouble()
        val enemyList = Motion.charge(cd, 5.0)
        enemyList.forEach {
            SkillUtils.damage(cd,it,dmg)
            Motion.knock(cd, it, distance)
        }
        return true
    }

}