package com.github.bryanser.rainbowskill.impl.assassin.dagger

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.PI

//对前方扇形区域丢出三把铁剑，
//击中就消失，
//不会穿透，铁剑飞行的长度是10
object FlyingLeaf : Skill("飞叶", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0)
        )) {
    override fun onCast(cd: CastData): Boolean {

        val dmg = getConfigEntry("Damage")(cd).toDouble()
        val player = cd.caster
        val is1 = ItemStack(Material.IRON_SWORD)

        val sas1 = player.world.spawn(SkillUtils.getLoc(player, true), ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = is1
        }
        val sas2 = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = is1
        }
        val sas3 = player.world.spawn(SkillUtils.getLoc(player, false), ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = is1
        }

        val vec = player.location.direction.normalize()
        object : BukkitRunnable() {
            var time = 0
            val angle = PI/4
            override fun run() {
                if (time++ >= 240) {
                    sas1.remove()
                    sas2.remove()
                    sas3.remove()
                    this.cancel()
                    return
                }
                val leftVec = vec.clone().add(Utils.getLeft(vec).multiply(0.50))
                sas1.velocity = leftVec
                SkillUtils.isDamage(sas1, cd, dmg)
                sas2.velocity = vec
                SkillUtils.isDamage(sas2, cd, dmg)
                val rightVec = vec.clone().add(Utils.getRight(vec).multiply(0.5))
                sas3.velocity = rightVec
                SkillUtils.isDamage(sas3, cd, dmg)
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }

}