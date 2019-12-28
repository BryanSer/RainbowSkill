package com.github.bryanser.rainbowskill.impl.knighterrant.sword

import com.github.bryanser.rainbowskill.Skill
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class SwiftWindShadowlessSword : Skill("疾风无影剑", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        val item = player.itemInHand

        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}