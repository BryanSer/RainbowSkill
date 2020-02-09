package com.github.bryanser.rainbowskill.impl.shooter.elf

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.shooter.ArrowPenetrate
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

//在接下来的五秒里，以自己为中心，360度发射30根箭，飞行距离都是5（即半径为5），使用技能的时候可以移动，每根箭伤害一样。
object KillingBloom : Skill(
        "杀戮绽放",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 20.0),
                ConfigEntry("Time", 100.0),
                ConfigEntry("Number", 300.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val distance = getConfigEntry("Distance")(cd).toDouble()
        val dmg = getConfigEntry("Damage")(cd).toDouble()
        val time = getConfigEntry("Time")(cd).toDouble()
        val number = getConfigEntry("Number")(cd).toDouble()

        val player = cd.caster

        object : BukkitRunnable() {
            var t = 0
            var angle: Double = 0.0
            var pi = Math.PI

            fun getVec(dd: Double): Vector {
                return Vector(cos(dd), 0.0, sin(dd))
            }

            var tt = (time * 20 / number).toInt()

            override fun run() {
                if (t++ >= time * 20) {
                    this.cancel()
                    return
                }

                if (t % tt == 0) {
                    angle += 2 * pi / 16
                    val loc = player.location

                    val currVec = getVec(angle)

                    ArrowPenetrate.cast(cd, Material.ARROW, loc, currVec, distance, true) {
                        SkillUtils.damage(cd, it, dmg)
                    }
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)


        return true
    }
}