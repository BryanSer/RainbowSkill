package com.github.bryanser.rainbowskill.impl.idleman

import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.Motion
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class DragonTooth : Skill("龙牙", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        Motion.charge(player,1.0,5.0)

        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}