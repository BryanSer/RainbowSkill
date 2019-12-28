package com.github.bryanser.rainbowskill.impl.knighterrant.sword

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class RoundMoonDagger : Skill("圆月匕首", mutableListOf(""), Material.REDSTONE) {



    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        val is1: ItemStack = ItemStack(Material.IRON_SWORD)
        val is2: ItemStack = ItemStack(Material.IRON_SWORD)
        val is3: ItemStack = ItemStack(Material.IRON_SWORD)

        val sas1 = player.world.spawn(SkillUtils.getLoc(player,true), ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = is1
        }
        val sas2 = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = is2
        }
        val sas3 = player.world.spawn(SkillUtils.getLoc(player,false), ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = is3
        }

        val vec = player.location.direction.normalize()
        object : BukkitRunnable() {
            var time = 0

            override fun run() {
                if (time++ >= 240) {
                    this.cancel()
                }
                sas1.velocity = vec
                SkillUtils.isDamage(sas1,player)
                sas2.velocity = vec
                SkillUtils.isDamage(sas2,player)
                sas3.velocity = vec
                SkillUtils.isDamage(sas3,player)
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}