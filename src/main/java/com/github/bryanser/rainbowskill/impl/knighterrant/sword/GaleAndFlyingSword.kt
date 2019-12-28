package com.github.bryanser.rainbowskill.impl.knighterrant.sword

import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class GaleAndFlyingSword : Skill("疾风飞剑", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        val sword1: ItemStack = ItemStack(Material.IRON_SWORD)
        val sword2: ItemStack = ItemStack(Material.IRON_SWORD)
        val sword3: ItemStack = ItemStack(Material.IRON_SWORD)

        val ins1 = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = sword1
        }
        val ins2 = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = sword2
        }
        val ins3 = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = true
            it.itemInHand = sword3
        }

        object : BukkitRunnable() {
            var time = 0
            var angle: Double = 0.0
            var pi = Math.PI

            var enemyDamage = HashMap<String, Double>(0)

            val maxDamage = 10

            fun isDamage(ins: ArmorStand) {
                for (e in ins.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        if (enemyDamage.containsKey(e.name)) {
                            val currDamage = enemyDamage.getValue(e.name)
                            if (currDamage < maxDamage) {
                                e.damage(1.0)
                                enemyDamage[e.name] = currDamage + 1.0
                            }
                        } else {
                            enemyDamage[e.name] = 1.0
                        }
                    }
                }
            }

            fun getLocation(loc: Location, angle: Double): Location {
                return loc.clone().add(2 * cos(angle), 0.0, 2 * sin(angle))
            }


            override fun run() {
                angle += 2 * pi / 30
                val loc = player.location

                if (time++ >= 80) {
                    this.cancel()
                }
                ins1.teleport(getLocation(loc, angle))
                isDamage(ins1)
                ins2.teleport(getLocation(loc, angle + 2 * pi / 3))
                isDamage(ins2)
                ins3.teleport(getLocation(loc, angle + 2 * pi / 3))
                isDamage(ins3)

                if (time % 20 == 0){
                    enemyDamage.clear()
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)


        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}