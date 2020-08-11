package com.github.bryanser.rainbowskill.impl.magician.fire

import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.ImmobilizeManager
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import org.bukkit.Material
import org.bukkit.scheduler.BukkitRunnable

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
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val time = (getConfigEntry("Time"))(cd).toDouble()
        val chantTime = getConfigEntry("ChantTime")(cd).toDouble()


        ImmobilizeManager.newData().also {
            it.timeLength = chantTime
            ImmobilizeManager.addEffectSelf(cd.caster, it)
        }

        val long: Double = 3.0
        val width: Double = 5.0
        val height: Double = 4.0

        val runTaskLater = object : BukkitRunnable() {
            override fun run() {
                Motion.wall(cd, Material.FIREBALL, time, chantTime, dmg, long, width, height, false)
            }
        }.runTaskLater(Main.getPlugin(), (time * 20).toLong())

        return true
    }
}