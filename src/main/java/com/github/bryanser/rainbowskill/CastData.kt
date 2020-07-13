package com.github.bryanser.rainbowskill

import org.bukkit.entity.Player
import java.util.*

data class CastData @JvmOverloads constructor(
        val caster: Player,
        val level:Int = 1,
        var cancel:Boolean = false,
        val castId:UUID = UUID.randomUUID()
) {
}