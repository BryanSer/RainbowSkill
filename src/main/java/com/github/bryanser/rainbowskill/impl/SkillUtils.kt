package com.github.bryanser.rainbowskill.impl

import com.github.bryanser.brapi.Utils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

object SkillUtils {

    //
    fun rangeAttack(player: Player, long: Double, width: Double) {
        val vec = player.location.direction.normalize()
        val left = Utils.getLeft(vec).multiply(width/2)
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
        //player.world.getNearbyEntities()

        for (e in player.getNearbyEntities(long*2, 1.0, width*2)) {
            if (e is LivingEntity) {
                val loc = e.location
                if (loc.x < x[0] && loc.x > x[1]
                        && loc.z < z[0] && loc.z > z[1]) {
                    e.damage(1.0)
                    break
                }
            }
        }
    }

    fun isDamage(ins: ArmorStand,player: Player) {
        for (e in ins.getNearbyEntities(0.25, 1.0, 0.25)) {
            if (e == player) {
                continue
            } else if (e is LivingEntity) {
                e.damage(1.0)
                break
            }
        }
    }

    fun getArmorStand(player: Player,location: Location,material: Material,isVisible :Boolean): ArmorStand {
        val itemstack: ItemStack = ItemStack(material)
        return player.world.spawn(location, ArmorStand::class.java) {
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


}