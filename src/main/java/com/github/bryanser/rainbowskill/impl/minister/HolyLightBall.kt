package com.github.bryanser.rainbowskill.impl.minister

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Color
import org.bukkit.Material

//技能1：圣光球
//发射一个金色粒子，距离为10，这个技能击中队友就回血，击中敌人就掉血，其伤害/恢复值都一样。
object HolyLightBall : Skill("圣光球", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("Distance", 15.0),
                ConfigEntry("Speed", 0.4)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()
        val speed = (getConfigEntry("Speed"))(cd).toDouble()

        val loc = cd.caster.eyeLocation.add(0.0, -0.5, 0.0)

        Motion.particleLine(cd, loc, Color.YELLOW, dmg, distance, speed){
            SkillUtils.damage(cd,it,dmg)
        }

        return true
    }

}