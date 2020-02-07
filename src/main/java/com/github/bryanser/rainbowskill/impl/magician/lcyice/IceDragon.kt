package com.github.bryanser.rainbowskill.impl.magician.lcyice

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.impl.idleman.BouquetOfTheGodOfFire
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

//向前方发射长为5的三条蓝色粒子缓慢向前飞，飞行距离是30，
// 并且飞过的地方下面的方块会变成冰块，技能结束后复原，
// 被击中的敌人会被冰冻3s
//（即攻击范围是前方的宽3，长30，但是这五格长的三条蓝色粒子会向前飞行移动，前面的敌人有机会躲开）
object IceDragon : Skill(
        "冰龙",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 0.0),
                ConfigEntry("FreezingTime", 3.0),
                ConfigEntry("ChantTime", 1.0),
                ConfigEntry("Time", 5.0),
                ConfigEntry("Distance", 30.0),
                ConfigEntry("Speed", 0.4)
        )) {

    override fun onCast(cd: CastData): Boolean {

        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val time = (getConfigEntry("Time"))(cd).toDouble()
        val chantTime = (getConfigEntry("ChantTime"))(cd).toLong()
        val freezingTime = getConfigEntry("FreezingTime")(cd).toDouble()

        val distance = getConfigEntry("Distance")(cd).toDouble()
        val speed = getConfigEntry("Speed")(cd).toDouble()


        val player = cd.caster

        val vec: Vector = player.location.direction.normalize()
        val centerLoc = player.eyeLocation.add(0.0, -0.5, 0.0)

        val leftLoc = player.eyeLocation.add(Utils.getLeft(vec).multiply(1.0)).add(0.0, -0.5, 0.0)

        val rightLoc = player.eyeLocation.add(Utils.getRight(vec).multiply(1.0)).add(0.0, -0.5, 0.0)

        val locList = mutableListOf<Location>()

        locList.add(centerLoc)
        locList.add(leftLoc)
        locList.add(rightLoc)

        val materialData = mutableMapOf<Location, Material>()

        object : BukkitRunnable() {
            var p = distance
            val damaged = hashSetOf<Int>()
            override fun run() {
                if (p <= 0) {
                    this.cancel()
                    return
                }
                val t = vec.clone().multiply(distance - p)

                locList.forEach {loc->
                    val curr = loc.clone().add(t)

                    ParticleEffect.FLAME.display(
                            0f,
                            0f,
                            0f,
                            0.0f, 3, curr, 50.0)

                    while (true){
                        val blockLoc = curr.clone().add(0.0, -1.0, 0.0)
                        if (curr.block!=Material.AIR){

                            materialData[blockLoc] = blockLoc.block.type
                            break;
                        }

                    }

                    for (e in curr.world.getNearbyEntities(curr, 0.1, 0.1, 0.1)) {
                        if (e is LivingEntity && e != player && e.entityId !in damaged) {
                            damaged += e.entityId
                            SkillUtils.damage(cd, e, dmg)
                            SpeedManager.newData().also {
                                it.modifier = -0.1
                                it.timeLength = 1.0
                                SpeedManager.addEffect(e, it)
                            }
                        }
                    }
                }
                p -= speed

            }
        }.runTaskTimerAsynchronously(Main.Plugin, 0, 1)

        return true
    }
}