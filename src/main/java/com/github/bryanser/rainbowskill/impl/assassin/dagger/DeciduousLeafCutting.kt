package com.github.bryanser.rainbowskill.impl.assassin.dagger

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material


//向前方冲刺5格，对范围内所有敌人造成伤害的同时，还让他们失明减速3s
object DeciduousLeafCutting : Skill("落叶斩", mutableListOf(""), Material.REDSTONE, listOf(
        ConfigEntry(COOLDOWN_KEY, 10.0),
        ConfigEntry("Damage", 10.0)
)) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val enemyList = Motion.charge(cd, 5.0)
        enemyList.forEach { e ->
            SkillUtils.damage(cd, e, dmg)
            //it.addPotionEffect(PotionEffect((PotionEffectType.BLINDNESS), 300, -3))
            SpeedManager.newData().also {
                it.modifier = -0.1
                it.timeLength = 3.0
                SpeedManager.addEffect(cd, e, it)
            }
            BlindnessManager.newData().also {
                it.modifier = -0.1
                it.timeLength = 3.0
                BlindnessManager.addEffect(cd, e, it)
            }

        }
        return true
    }

}