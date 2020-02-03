package com.github.bryanser.rainbowskill.impl.idleman

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material

//把前方宽3，长2区域内的敌人击退5格并击飞2格高，造成伤害
object FallenPalm : Skill("落花掌", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("Distance",5.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
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