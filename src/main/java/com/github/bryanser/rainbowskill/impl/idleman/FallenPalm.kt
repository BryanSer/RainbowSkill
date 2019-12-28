package com.github.bryanser.rainbowskill.impl.idleman

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.Motion
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

object FallenPalm : Skill("落花掌", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("distance",5.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("damage"))(cd).toDouble()
        val distance = (getConfigEntry("distance"))(cd).toDouble()

        val enemyList = SkillUtils.rangeAttack(cd, 2.0, 3.0)

        enemyList.forEach {
            SkillUtils.damage(cd,it,dmg)
            Motion.knock(cd,it,distance)
        }
        return true
    }

}