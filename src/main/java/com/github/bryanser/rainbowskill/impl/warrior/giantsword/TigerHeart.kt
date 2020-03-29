package com.github.bryanser.rainbowskill.impl.warrior.giantsword

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin
import org.bukkit.Color

//使用技能后身边出现红色粒子，持续3s，被击中的敌人都会被击飞，提升自己的15%暴击几率
object TigerHeart : Skill("虎贲", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()

        Motion.particleCircle(cd.caster,3.0,255.0,Color.RED){
            SkillUtils.damage(cd,it,dmg)
            Motion.knock(cd, it, distance)
        }

//        val asList = mutableListOf<ArmorStand>()
//        for (i in 0 until 8){
//            val ins = SkillUtils.getArmorStand(player, player.location, Material.IRON_AXE, false)
//            asList.add(ins)
//        }
//        object : BukkitRunnable() {
//            var time = 0
//            var angle: Double = 0.0
//            var pi = Math.PI
//
//            fun getLocation(loc: Location, angle: Double): Location {
//                return loc.clone().add(2 * cos(angle), 0.0, 2 * sin(angle))
//            }
//
//            override fun run() {
//                val loc = player.location
//
//                if (time++ >= 60) {
//                    this.cancel()
//                }
//                asList.forEach{
//                    angle += 2 * pi / 8
//                    it.teleport(getLocation(loc, angle))
//                }
//            }
//        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }
}