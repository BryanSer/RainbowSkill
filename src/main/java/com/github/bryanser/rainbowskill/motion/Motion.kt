package com.github.bryanser.rainbowskill.motion

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.particle.Particle
import com.github.bryanser.rainbowskill.script.ExpressionResult
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

typealias Expression = (Player) -> ExpressionResult

object Motion {
    fun Location.distanceSquared2(loc: Location): Double {
        if (this.world != loc.world) {
            return Double.MAX_VALUE
        }
        return this.distanceSquared(loc)
    }

    //lateinit var damage: Expression
    lateinit var length: Expression
    lateinit var knock: Expression
    lateinit var stop: Expression
    lateinit var air: Expression

    /**
     * 撞击
     */
    fun charge(cd: CastData/*, dmg: Double*/, lengthSq1: Double): MutableList<LivingEntity> {
        val player = cd.caster
        var lengthSq = lengthSq1
        lengthSq *= lengthSq
        val knock = 3

        //val stop = stop(player).toBoolean()

        val start = player.location.clone()
        val vec = player.location.direction.clone()
        vec.setY(0)
        vec.normalize()
        val enemyList = mutableListOf<LivingEntity>()
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
                        e.velocity = tvec
                        //SkillUtils.damage(cd,e,dmg)
                        damaged += e.entityId
                        enemyList.add(e)
//                        if (stop) {
//                            player.velocity = Vector()
//                            this.cancel()
//                            return
//                        }
                    }
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)
        return enemyList
    }

    /**
     * 闪现
     */
    fun flash(player: Player, direction: Int, length: Double) {
        //val length = length(player).toDouble()
        val vec = player.location.direction.normalize()
        vec.y = 0.0
        vec.normalize()
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

    fun wall(cd: CastData,
             material: Material,
             times: Double,
             damage: Double,
             long: Double,
             width: Double,
             height: Double) {
        val player = cd.caster
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

        val itemStack: ItemStack = ItemStack(material)
        val loc = player.location


        val normalize = player.location.direction.normalize()
        val currLoc = loc.clone().add(normalize.multiply(long))

        currLoc.add(Utils.getLeft(normalize).multiply(width / 2))

        for (i in 1.. height.toInt() ) {

            for (j in 1 .. width.toInt()) {
                val ins = player.world.spawn(
                        currLoc,
                        ArmorStand::class.java) {
                    it.isMarker = true
                    it.setGravity(false)
//                    it.isVisible = false
                    it.itemInHand = itemStack
                }
                asList.add(ins)
                currLoc.add(Utils.getRight(normalize).multiply(1.0))
                //println("生成as")
            }
            currLoc.add(Utils.getLeft(normalize).multiply(width))
            currLoc.add(0.0,1.0,0.0)
        }

        object : BukkitRunnable() {
            var time = 0
            override fun run() {
                var index = 0

                if (time++ >= times * 20) {
                    asList.forEach {
                        it.remove()
                    }
                    this.cancel()
                    return
                }
                //println("运行")
                asList.forEach {

                    for (e in it.getNearbyEntities(0.25, 1.0, 0.25)) {
                        if (e == player) {
                            continue
                        } else if (e is LivingEntity) {
                            SkillUtils.damage(cd, e, damage)
                            break
                        }
                    }
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)
    }

    fun drawWall(location: Location, long: Double, width: Double, par: Particle) {

    }

    fun knock(cd: CastData, target: LivingEntity, dis: Double) {
        val p = cd.caster
        val vec = target.location.toVector().subtract(p.location.toVector())
        vec.y = 1.0
        vec.normalize().multiply(dis)
        target.velocity = vec
    }

    lateinit var particle: Particle

    fun particleCircle(cd: CastData, r: Double, p: Double) {
        val player = cd.caster
        val loc = player.location


        Bukkit.getScheduler().runTaskAsynchronously(Main.Plugin) {
            var st = 0.0
            val add = Math.PI / p
            while (st <= Math.PI * 2) {
                val x = cos(st) * r
                val z = sin(st) * r
                val loc = loc.clone().add(x, 0.0, z)
                particle.play(loc)
                st += add
            }
        }
    }


}

inline fun Location.distanceSquared2(loc: Location): Double {
    if (this.world != loc.world) {
        return Double.MAX_VALUE
    }
    return this.distanceSquared(loc)
}