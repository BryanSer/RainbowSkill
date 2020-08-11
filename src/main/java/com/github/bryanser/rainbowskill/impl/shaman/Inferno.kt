package com.github.bryanser.rainbowskill.impl.shaman

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Color
import org.bukkit.Material

//对着自我半径3释放一个圆形的灰色粒子波，敌人会被击退，并掉血
object Inferno : Skill("地狱烈焰", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("Radius", 3.0),
                ConfigEntry("Distance", 3.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val radius = getConfigEntry("Radius")(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()

        Motion.particleCircle(1, cd.caster, radius, 255.0, Color.GRAY) {
            SkillUtils.damage(cd, it, dmg)
            Motion.knock(cd, it, distance)
        }

        return true
    }

}