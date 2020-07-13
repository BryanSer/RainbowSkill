package com.github.bryanser.rainbowskill.motion

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.CastData
import ltd.icecold.rainbowheros.game.entity.HostilityEntity
import ltd.icecold.rainbowheros.game.manager.GameManager
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

fun Player.isFriendly(other: LivingEntity, self: Boolean = false): Boolean {
    if (this === other) {
        return !self
    }
    if (other is HostilityEntity) {
        if (other.isFriend(this)) {
            return true
        }
    }
    val g = GameManager.getManager().getPlayerStayedGame(this) ?: return true
    if(other is Player){
        return g.isFriend(this,other)
    }
    return true
}

object SkillUtils {

    fun damage(cd: CastData, target: LivingEntity, damage: Double): Boolean {
        val caster = cd.caster
        if (caster.isFriendly(target)) {
            return false
        }
        target.damage(damage)
        return true
    }

    //
    fun rangeAttack(cd: CastData, long: Double, width: Double): MutableList<LivingEntity> {
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
        val enemyList = mutableListOf<LivingEntity>()

        for (e in player.getNearbyEntities(long * 2, 1.0, width * 2)) {
            if (e is LivingEntity) {
                val loc = e.location
                if (loc.x < x[0] && loc.x > x[1]
                        && loc.z < z[0] && loc.z > z[1]) {
                    enemyList.add(e)
                    break
                }
            }
        }
        return enemyList
    }


    fun isDamage(ins: ArmorStand, cd: CastData, damage: Double, penetrate: Boolean) {
        for (e in ins.getNearbyEntities(0.25, 1.0, 0.25)) {
            if (e == cd.caster) {
                continue
            } else if (e is LivingEntity) {
                damage(cd, e, damage)
                if (!penetrate) {
                    ins.remove()
                }
                break
            }
        }
    }

    fun getArmorStand(player: Player, location: Location, material: Material, isVisible: Boolean): ArmorStand {
        val itemstack: ItemStack = ItemStack(material)
        return ArmorStandManager.createArmorStand(location) {
            it.isVisible = isVisible
            it.itemInHand = itemstack
        }
    }

    /**
     * true 为左边
     * false为右
     */
    fun getLoc(player: Player, falg: Boolean): Location {
        val vec: Vector = player.location.direction.normalize()
        return if (falg) {
            player.location.add(Utils.getLeft(vec).multiply(1.0))
        } else {
            player.location.add(Utils.getRight(vec).multiply(1.0))
        }
    }

    fun getDistance(loc1: Location, loc2: Location): Double {
        return (loc1.x - loc2.x) * (loc1.x - loc2.x) + (loc1.y - loc2.y) * (loc1.y - loc2.y) + (loc1.z - loc2.z) * (loc1.z - loc2.z)
    }

    fun getVec(dd: Double): Vector {
        return Vector(cos(dd), 0.0, sin(dd))
    }


//    fun <F> Finder(f:(LivingEntity)-> Collection<F>):Finder<F>{
//        return object : Finder<F> {
//            override fun finder(p: LivingEntity): Collection<F> {
//                return f(p)
//            }
//
//            override fun invoke(p: CastData): Collection<F> {
//                return f(p.finder()).filter {
//
//                }
//            }
//        }
//    }

//    fun getSightLocation(range: Int):Finder<Location> {
//        val transient = mutableSetOf(Material.AIR)
//        return Finder{
//            listOf(it.getTargetBlock(transient,range).location)
//        }
//    }

}

