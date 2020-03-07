package com.github.bryanser.rainbowskill.impl.warrior.lance

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.SpeedManager
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material

//技能2：无双乱舞
//对前方长3宽3造成伤害并减速，需要蓄力1.5s。
object Unrivaled : Skill("无双乱舞", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("damage"))(cd).toDouble()
        val player = cd.caster
        val rangeAttack = SkillUtils.rangeAttack(cd, 3.0, 3.0)
        rangeAttack.forEach {
            SkillUtils.damage(cd, it, dmg)
            SpeedManager.newData().also { speedData ->
                speedData.modifier = -0.1
                speedData.timeLength = 1.0
                SpeedManager.addEffect(cd,it,speedData)
            }
        }
        return true
    }

}