package com.github.bryanser.rainbowskill.impl.magician.lcyice

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import org.bukkit.Material

object IceWall : Skill(
        "冰墙",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 0.0),
                ConfigEntry("Time", 5.0)
        )) {

    override fun onCast(cd: CastData): Boolean {
        val long: Double = 3.0
        val width: Double = 6.0
        val height: Double = 4.0

        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val time = (getConfigEntry("Time"))(cd).toDouble()

        Motion.wall(cd, Material.ICE, time, dmg, long, width, height)
        return true
    }
}