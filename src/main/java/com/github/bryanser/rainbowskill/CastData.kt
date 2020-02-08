package com.github.bryanser.rainbowskill

import org.bukkit.entity.Player
import java.util.*

data class CastData(
        val caster: Player,
        val level:Int,
        var cancel:Boolean = false,
        val castId:UUID = UUID.randomUUID()
) {
}