package com.github.bryanser.rainbowskill.motion

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.FrozenManager
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.impl.idleman.BouquetOfTheGodOfFire
import com.github.bryanser.rainbowskill.particle.Particle
import com.github.bryanser.rainbowskill.script.ExpressionResult
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
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
             chantTime: Double,
             damage: Double,
             long: Double,
             width: Double,
             height: Double,
             penetrate: Boolean) {
        val player = cd.caster

        val asList = mutableListOf<ArmorStand>()

        val itemStack: ItemStack = ItemStack(material)
        val loc = player.location

        val normalize = player.location.direction.normalize()
        val currLoc = loc.clone().add(normalize.multiply(long))

        currLoc.add(Utils.getLeft(normalize).multiply(width / 2))

        for (i in 1..height.toInt()) {
            for (j in 1..width.toInt()) {
                val ins = ArmorStandManager.createArmorStand(
                        currLoc) {
                    it.isMarker = penetrate
                    it.setGravity(false)
                    it.isVisible = false
                    it.itemInHand = itemStack
                }
                asList.add(ins)
                currLoc.add(Utils.getRight(normalize).multiply(1.0))
            }
            currLoc.add(Utils.getLeft(normalize).multiply(width))
            currLoc.add(0.0, 1.0, 0.0)
        }

        object : BukkitRunnable() {
            var time = 0
            val damaged = hashSetOf<Int>()
            override fun run() {
                if (time++ >= times * 20) {
                    asList.forEach {
                        it.remove()
                    }
                    this.cancel()
                    return
                }
                asList.forEach {
                    for (e in it.getNearbyEntities(0.25, 1.0, 0.25)) {
                        if (e == player) {
                            continue
                        } else if (e is LivingEntity && e.entityId !in damaged) {
                            damaged += e.entityId
                            SkillUtils.damage(cd, e, damage)
                            break
                        }
                    }
                }
                if (time % 20 == 0) {
                    damaged.clear()
                }
            }
        }.runTaskTimer(Main.Plugin, (20 * chantTime).toLong(), 1)
    }


    fun knock(cd: CastData, target: LivingEntity, dis: Double) {
        val p = cd.caster
        val vec = target.location.toVector().subtract(p.location.toVector())
        vec.y = 1.0
        vec.normalize().multiply(dis)
        target.velocity = vec
    }

    lateinit var particle: Particle

    fun particleCircle(cd: CastData, r: Double, p: Double, effect: (LivingEntity) -> Unit) {
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

                for (e in loc.world.getNearbyEntities(loc, 0.1, 0.1, 0.1)) {

                    if (e is LivingEntity && e != player) {
                        effect(e)
                    }
                }
            }
        }
    }


    fun particleZone(cd: CastData, dmg: Double, color: Color, long: Int, width: Int, effect: (LivingEntity) -> Unit) {
        val player = cd.caster
        val loc = player.location
        object : BukkitRunnable() {
            var t = 0
            val damaged = hashSetOf<Int>()

            override fun run() {
                if (t++ > 20 * 1) {
                    this.cancel()
                }

                val normalize = loc.direction.normalize()
                val currLoc = loc.clone().add(normalize.multiply(1.0))

                currLoc.add(Utils.getLeft(normalize).multiply(width / 2))

                for (i in 1..long) {
                    for (j in 1..width) {
                        ParticleEffect.REDSTONE.display(ParticleEffect.OrdinaryColor(color), currLoc, 50.0)
                        currLoc.add(Utils.getRight(normalize).multiply(1.0))

                        for (e in currLoc.world.getNearbyEntities(loc, 1.0, 1.0, 1.0)) {
                            if (e is LivingEntity && e != player && e.entityId !in damaged) {
                                effect(e)
                                damaged += e.entityId
                                SkillUtils.damage(cd, e, dmg)
                            }
                        }
                    }
                    currLoc.add(Utils.getLeft(normalize).multiply(width))
                    currLoc.add(0.0, 0.0, 0.0)
                    currLoc.add(normalize.multiply(1.0))
                }
            }
        }.runTaskTimerAsynchronously(Main.Plugin, 0, 1)
    }

    fun particleLine(cd: CastData, loc: Location, color: Color, dmg: Double, distance: Double, speed: Double, effect: (LivingEntity) -> Unit) {
        val player = cd.caster

        val vec = loc.direction.normalize()

        object : BukkitRunnable() {
            var p = distance
            val damaged = hashSetOf<Int>()
            override fun run() {
                if (p <= 0) {
                    this.cancel()
                    return
                }
                val t = vec.clone().multiply(distance - p)
                val loc = loc.clone().add(t)
                ParticleEffect.REDSTONE.display(ParticleEffect.OrdinaryColor(color), loc, 50.0)
                p -= speed
                for (e in loc.world.getNearbyEntities(loc, 0.1, 0.1, 0.1)) {
                    if (e is LivingEntity && e != player && e.entityId !in damaged) {
                        damaged += e.entityId
                        SkillUtils.damage(cd, e, dmg)
                        effect(e)
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.Plugin, 0, 1)
    }

    fun particleLinePro(cd: CastData,
                        delay: Double,
                        loc: Location,
                        color: Color,
                        trackColor: Color,
                        dmg: Double,
                        distance: Double,
                        speed: Double,
                        effect: (LivingEntity) -> Unit) {
        val player = cd.caster

        val vec = loc.direction.normalize()

        val list = ArrayDeque<Location>()

        object : BukkitRunnable() {
            var p = distance
            val damaged = hashSetOf<Int>()
            override fun run() {
                if (p <= 0) {
                    this.cancel()
                    return
                }
                val t = vec.clone().multiply(distance - p)
                val loc = loc.clone().add(t)
                list.add(loc.clone())
                ParticleEffect.REDSTONE.display(ParticleEffect.OrdinaryColor(color), loc, 50.0)

                if (list.size >= 5) {
                    if (list.size >= 20) {
                        list.pop()
                    }
                    list.forEach {
                        ParticleEffect.REDSTONE.display(
                                ParticleEffect.OrdinaryColor(trackColor),
                                it,
                                50.0)
                    }
                }

                p -= speed
                for (e in loc.world.getNearbyEntities(loc, 0.1, 0.1, 0.1)) {
                    if (e is LivingEntity && e != player && e.entityId !in damaged) {
                        damaged += e.entityId
                        SkillUtils.damage(cd, e, dmg)
                        effect(e)
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.Plugin, (delay * 20).toLong(), 1)
    }
}


inline fun Location.distanceSquared2(loc: Location): Double {
    if (this.world != loc.world) {
        return Double.MAX_VALUE
    }
    return this.distanceSquared(loc)
}

