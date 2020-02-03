package com.github.bryanser.rainbowskill.impl.idleman

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.SpeedManager
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material

//将前方三个的敌人挑飞3格高，造成伤害，并减速10%，持续3s
object ClapOfThunder : Skill("惊雷", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("Distance", 3.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()

        val enemyList = SkillUtils.rangeAttack(cd, 2.0, 3.0)

        for (i in 0 until 3) {
            SkillUtils.damage(cd, enemyList[i], dmg)
            Motion.knock(cd, enemyList[i], distance)
            SpeedManager.newData().also {
                it.modifier = -0.1
                it.timeLength = 3.0
                SpeedManager.addEffect(enemyList[i], it)
            }
        }
        return true
    }

}