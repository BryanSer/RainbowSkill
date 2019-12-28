package com.github.bryanser.rainbowskill.impl.idleman

import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class FallenPalm : Skill("落花掌", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        SkillUtils.rangeAttack(player,2.0,3.0)
        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}