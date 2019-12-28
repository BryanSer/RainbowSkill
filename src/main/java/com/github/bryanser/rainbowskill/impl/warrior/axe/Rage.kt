package com.github.bryanser.rainbowskill.impl.warrior.axe

import com.github.bryanser.rainbowskill.Skill
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class Rage : Skill("狂怒", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}