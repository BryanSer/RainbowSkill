package com.github.bryanser.rainbowskill.impl.magician.thunder

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.LivingEntity

//向前方丢出一个黄色粒子，飞行距离是10，击中了敌人后就落下一道雷（一道雷纯属好看）造成伤害
object ElectricalWave : Skill("电气波", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 10.0),
                ConfigEntry("Speed",0.4)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = getConfigEntry("Distance")(cd).toDouble()
        val speed = getConfigEntry("Speed")(cd).toDouble()

        val loc = cd.caster.eyeLocation.add(0.0, -0.5, 0.0)

        Motion.particleLine(cd, loc, Color.YELLOW, dmg, distance, speed){
            loc.world.strikeLightningEffect(loc)

            SkillUtils.damage(cd,it,dmg)
        }

        return true
    }

}