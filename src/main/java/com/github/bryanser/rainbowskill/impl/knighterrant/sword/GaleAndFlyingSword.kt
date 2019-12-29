package com.github.bryanser.rainbowskill.impl.knighterrant.sword

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.github.bryanser.rainbowskill.impl.idleman.FallenPalm
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

//召唤三把铁剑围着自己转，
// 持续4s，半径是以玩家为中心的2格，
// 在这个范围内的敌人被飞剑击中就会掉血
object GaleAndFlyingSword : Skill(
        "疾风飞剑",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 0.1),
                ConfigEntry("MaxDamage", 1.0),
                ConfigEntry("time", 4.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster

        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val maxDamage = (getConfigEntry("MaxDamage"))(cd).toDouble()
        val time = (getConfigEntry("time"))(cd).toDouble()

        val sword: ItemStack = ItemStack(Material.IRON_SWORD)

        val ins1 = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = sword
        }
        val ins2 = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = sword
        }
        val ins3 = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = sword
        }

        object : BukkitRunnable() {
            var t = 0
            var angle: Double = 0.0
            var pi = Math.PI

            var enemyDamage = HashMap<String, Double>(0)

            fun isDamage(ins: ArmorStand) {
                for (e in ins.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        if (enemyDamage.containsKey(e.name)) {
                            val currDamage = enemyDamage.getValue(e.name)
                            if (currDamage < maxDamage) {
                                SkillUtils.damage(cd, e, dmg)
                                enemyDamage[e.name] = currDamage + dmg
                            }
                        } else {
                            SkillUtils.damage(cd, e, dmg)
                            enemyDamage[e.name] = dmg
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

                if (t++ >= time * 20) {
                    this.cancel()
                }

                ins1.teleport(getLocation(loc, angle))
                isDamage(ins1)
                ins2.teleport(getLocation(loc, angle + 2 * pi / 3))
                isDamage(ins2)
                ins3.teleport(getLocation(loc, angle + 2 * pi / 3))
                isDamage(ins3)

                if (t % 20 == 0) {
                    enemyDamage.clear()
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }

}