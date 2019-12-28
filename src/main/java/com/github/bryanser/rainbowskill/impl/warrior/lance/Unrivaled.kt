package com.github.bryanser.rainbowskill.impl.warrior.lance

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.github.bryanser.rainbowskill.impl.warrior.giantsword.AdmirablyWonderful
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

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
        }
        return true
    }

}