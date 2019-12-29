package com.github.bryanser.rainbowskill.impl.magician.fire

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.Motion
import org.bukkit.Material

object FireWall : Skill(
        "火墙",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Time", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val long: Double = 3.0
        val width: Double = 5.0
        val height: Double = 4.0

        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val time = (getConfigEntry("Time"))(cd).toDouble()

        Motion.wall(cd, Material.IRON_SWORD, time, dmg, long, width, height)
        return true
    }

}