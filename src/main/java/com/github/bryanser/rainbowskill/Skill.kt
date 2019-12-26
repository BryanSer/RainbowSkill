package com.github.bryanser.rainbowskill

import com.relatev.minecraft.RainbowHero.skill.CastResultType
import com.relatev.minecraft.RainbowHero.skill.Castable
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

abstract class Skill(
        val name: String,
        internal val description: MutableList<String>,
        internal val displayMaterial: Material
) : Castable {
    abstract fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any>

    override fun cast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        TODO()
        return onCast(player, level)
    }

    override fun getDescription(): MutableList<String> = description

    override fun getDisplayMaterial(): Material = displayMaterial
}