package com.github.bryanser.rainbowskill.impl.knighterrant.sword

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
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

object RoundMoonDagger : Skill("圆月匕首", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("time", 4.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster
        val ironSword: ItemStack = ItemStack(Material.IRON_SWORD)
        val dmg = (getConfigEntry("damage"))(cd).toDouble()
        val time = (getConfigEntry("time"))(cd).toDouble()

        val sas1 = player.world.spawn(SkillUtils.getLoc(player, true), ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = ironSword
        }
        val sas2 = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = ironSword
        }
        val sas3 = player.world.spawn(SkillUtils.getLoc(player, false), ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = ironSword
        }

        val vec = player.location.direction.normalize()
        object : BukkitRunnable() {
            var t = 0

            override fun run() {
                if (t++ >= time*20) {
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