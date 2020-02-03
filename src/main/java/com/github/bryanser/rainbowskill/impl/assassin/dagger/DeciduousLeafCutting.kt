package com.github.bryanser.rainbowskill.impl.assassin.dagger

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


//向前方冲刺5格，对范围内所有敌人造成伤害的同时，还让他们失明减速3s
object DeciduousLeafCutting : Skill("落叶斩", mutableListOf(""), Material.REDSTONE, listOf(
        ConfigEntry(COOLDOWN_KEY, 10.0),
        ConfigEntry("Damage", 10.0)
)) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val enemyList = Motion.charge(cd, 5.0)
        enemyList.forEach {
            SkillUtils.damage(cd, it, dmg)
            it.addPotionEffect(PotionEffect((PotionEffectType.BLINDNESS), 300, -3))
        }
        return true
    }

}