package com.github.bryanser.rainbowskill.impl.idleman

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material

// 向前方突刺5格，路径上的敌人受到伤害
object DragonTooth : Skill("龙牙", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val enemyList = Motion.charge(cd, 5.0)
        enemyList.forEach {
            SkillUtils.damage(cd,it,dmg)
        }
        return true
    }

}