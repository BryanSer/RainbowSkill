package com.github.bryanser.rainbowskill.impl.warrior.giantsword

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.SilentManager
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Color
import org.bukkit.Material


//对前方长5宽1范围打出绿色粒子，造成伤害的同时还将击飞，并沉默敌人1s
object AdmirablyWonderful : Skill(
        "石破天惊",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 3.0),
                ConfigEntry("SilenceTime", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()
        val silenceTime = getConfigEntry("SilenceTime")(cd).toDouble()

        Motion.particleZone(cd,dmg,Color.GREEN,5,1){
            Motion.knock(cd, it, distance)
            SilentManager.newData().also { silentData ->
                silentData.modifier = -0.1
                silentData.timeLength = silenceTime
                SilentManager.addEffect(it, silentData)
            }
        }

//        val enemyList = SkillUtils.rangeAttack(cd, 5.0, 1.0)
//        enemyList.forEach {
//            SkillUtils.damage(cd, it, dmg)


//        }

        return true
    }
}