package com.github.bryanser.rainbowskill

import org.bukkit.entity.Player

data class CastData(
        val caster: Player,
        val level:Int
) {
}