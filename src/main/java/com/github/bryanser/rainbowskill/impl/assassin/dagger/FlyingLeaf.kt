package com.github.bryanser.rainbowskill.impl.assassin.dagger

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

//对前方扇形区域丢出三把铁剑，
//击中就消失，
//不会穿透，铁剑飞行的长度是10
object FlyingLeaf : Skill("飞叶", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0)
        )) {

//
//    val damage: ConfigEntry by lazy {
//        for (cfg in configs) {
//            if (cfg.key == "Damage") {
//                return@lazy cfg
//            }
//        }
//        throw IllegalStateException()
//    }

    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster
        val is1 = ItemStack(Material.IRON_SWORD)

        val sas1 = player.world.spawn(SkillUtils.getLoc(player, true), ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = is1
        }
        val sas2 = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = is1
        }
        val sas3 = player.world.spawn(SkillUtils.getLoc(player, false), ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = is1
        }

        val vec = player.location.direction.normalize()
        val dmg = getConfigEntry("damage")(cd).toDouble()
        object : BukkitRunnable() {
            var time = 0

            override fun run() {
                if (time++ >= 240) {
                    this.cancel()
                }
                sas1.velocity = vec
                SkillUtils.isDamage(sas1, cd, dmg)
                sas2.velocity = vec
                SkillUtils.isDamage(sas2, cd, dmg)
                sas3.velocity = vec
                SkillUtils.isDamage(sas3, cd, dmg)
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }

}