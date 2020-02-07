package com.github.bryanser.rainbowskill.impl.magician.fire

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import org.bukkit.Material

//在面前3格形成一堵宽为5 高为4的火墙，敌人触碰就掉血，吟唱2s，墙持续2.5s
object FireWall : Skill(
        "火墙",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("ChantTime", 2.0),
                ConfigEntry("Time", 2.5)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val long: Double = 3.0
        val width: Double = 5.0
        val height: Double = 4.0

        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val time = (getConfigEntry("Time"))(cd).toDouble()
        val chantTime = getConfigEntry("ChantTime")(cd).toLong()

        Motion.wall(cd, Material.FIRE, time, chantTime, dmg, long, width, height, false)
        return true
    }

}