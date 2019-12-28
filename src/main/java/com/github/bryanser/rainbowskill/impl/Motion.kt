package com.github.bryanser.rainbowskill.impl

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.script.ExpressionResult
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

typealias Expression = (Player) -> ExpressionResult

object Motion {
    fun Location.distanceSquared2(loc: Location): Double {
        if (this.world != loc.world) {
            return Double.MAX_VALUE
        }
        return this.distanceSquared(loc)
    }

    lateinit var damage: Expression
    lateinit var length: Expression
    lateinit var knock: Expression
    lateinit var stop: Expression
    lateinit var air: Expression

    fun charge(player: Player, dmg: Double, lengthSq1: Double) {
        var lengthSq = lengthSq1
        lengthSq *= lengthSq
        val knock = knock(player).toDouble()
        val stop = stop(player).toBoolean()
        val start = player.location.clone()
        val vec = player.location.direction.clone()
        vec.setY(0)
        vec.normalize()
        object : BukkitRunnable() {
            var time = 0
            val damaged = mutableSetOf<Int>()
            override fun run() {
                if (player.world != start.world) {
                    this.cancel()
                    player.velocity = Vector()
                    return
                }
                if (time++ >= 100) {
                    this.cancel()
                    player.velocity = Vector()
                    return
                }
                if (player.location.add(vec).add(0.0, 1.0, 0.0).block.type != Material.AIR) {
                    player.velocity = Vector()
                    this.cancel()
                    return
                }
                if (start.distanceSquared2(player.location) >= lengthSq) {
                    player.velocity = Vector()
                    this.cancel()
                    return
                }
                player.velocity = vec
                for (e in player.getNearbyEntities(0.25, 0.25, 0.25)) {
//                    if (isCitizens(e)) {
//                        continue
//                    }
                    if (e is LivingEntity && e !== player && !damaged.contains(e.entityId)) {
                        //e.motionDamage(dmg, p, ci.castId)
                        val tvec = e.location.subtract(player.location).toVector()
                        tvec.y = 1.0
                        tvec.normalize().multiply(knock)
                        //e.velocity = tvec
                        //e.knock(tvec, ci.castId)
                        damaged += e.entityId
                        if (stop) {
                            player.velocity = Vector()
                            this.cancel()
                            return
                        }
                    }
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)
    }

    fun jump(player: Player, direction: Int, length: Double) {
        //val length = length(player).toDouble()
        val vec = player.location.direction.normalize()
        if (!air(player).toBoolean()) {
            vec.y = 0.0
            vec.normalize()
        }
        var l = 0.0
        val move = length / (length + 1)
        var last = player.location
        while (l <= length) {
            l += move
            val t = vec.clone().multiply(l * direction)
            val loc = player.location.add(t)
            if (loc.block.type != Material.AIR) {
                break
            } else {
                last = loc
            }
        }
        player.teleport(last)
    }

    fun wall(player: Player, material: Material, times: Double, long: Double, width: Double, height: Double) {


        val vec = player.location.direction.normalize()
        val left = Utils.getLeft(vec).multiply(width / 2)
        val leftPoint = player.location.add(left)
        val longPoint = player.location.add(left.multiply(-1)).add(vec.multiply(long))
        val x = doubleArrayOf(leftPoint.x, longPoint.x).let {
            Arrays.sort(it)
            it
        }
        val z = doubleArrayOf(leftPoint.z, longPoint.z).let {
            Arrays.sort(it)
            it
        }

        val asList = mutableListOf<ArmorStand>()

        var h = 0.0
        for (i in 1 until width.toInt() + 1) {
            for (j in 1 until height.toInt() + 1) {
                val ins = SkillUtils.getArmorStand(player, player.location.add((x[0] + x[1]) / width * i, h, z[0]), material, false)
                asList.add(ins)
            }
            h++
        }

        object : BukkitRunnable() {
            var time = 0

            override fun run() {
                if (time++ >= times * 20) {
                    this.cancel()
                }
                asList.forEach {
                    for (e in it.getNearbyEntities(0.25, 1.0, 0.25)) {
                        if (e == player) {
                            continue
                        } else if (e is LivingEntity) {
                            e.damage(1.0)
                            break
                        }
                    }
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)
    }
}