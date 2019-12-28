package com.github.bryanser.rainbowskill.impl.shooter.elf

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object PiercingArrow : Skill(
        "穿透箭",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster
        val arrow: ItemStack = ItemStack(Material.IRON_SWORD)

        val arrowAS = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = arrow
        }

        val vec = player.location.direction.normalize()

        object : BukkitRunnable() {
            var time = 0
            override fun run() {
                if (time++ >= 600) {
                    this.cancel()
                }
                arrowAS.velocity = vec
                for (e in arrowAS.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        e.damage(1.0)
                        break
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }
}