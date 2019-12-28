package com.github.bryanser.rainbowskill.impl.magician.lcyice

import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.Motion
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class IceWall : Skill("冰墙", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        val long: Double = 3.0
        val width: Double = 6.0
        val height: Double = 4.0
        Motion.wall(player, Material.FIRE, 2.5, long, width, height)
        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }
}