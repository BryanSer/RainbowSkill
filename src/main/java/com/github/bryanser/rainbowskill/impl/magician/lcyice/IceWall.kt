package com.github.bryanser.rainbowskill.impl.magician.lcyice

import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.ImmobilizeManager
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.magician.fire.FireWall
import com.github.bryanser.rainbowskill.motion.Motion
import org.bukkit.Material
import org.bukkit.scheduler.BukkitRunnable

//在面前3格形成一堵宽为6 高为4的冰墙，敌人无法穿过，触碰不掉血，吟唱1s，墙持续5s
object IceWall : Skill(
        "冰墙",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 0.0),
                ConfigEntry("ChantTime", 1.0),
                ConfigEntry("Time", 5.0)
        )) {

    override fun onCast(cd: CastData): Boolean {
        val long: Double = 3.0
        val width: Double = 6.0
        val height: Double = 4.0

        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val time = (getConfigEntry("Time"))(cd).toDouble()
        val chantTime = (getConfigEntry("ChantTime"))(cd).toDouble()

        ImmobilizeManager.newData().also {
            it.timeLength = chantTime
            ImmobilizeManager.addEffectSelf(cd.caster, it)
        }

        object : BukkitRunnable() {
            override fun run() {
                Motion.wall(cd, Material.ICE, time, chantTime, dmg, long, width, height, true)
            }
        }.runTaskLater(Main.getPlugin(), (time * 20).toLong())

        return true
    }
}