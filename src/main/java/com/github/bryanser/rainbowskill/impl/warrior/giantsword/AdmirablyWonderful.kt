package com.github.bryanser.rainbowskill.impl.warrior.giantsword

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


//对前方长5宽1范围打出绿色粒子，造成伤害的同时还将击飞，并沉默敌人1s
class AdmirablyWonderful : Skill("石破天惊", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        SkillUtils.rangeAttack(player,5.0,1.0)


        val itemstack: ItemStack = ItemStack(Material.SLIME_BALL)
        val ins = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = itemstack
        }
        val vec = player.location.direction.normalize()
        object : BukkitRunnable() {
            var time = 0
            override fun run() {
                if (time++ >= 240) {
                    this.cancel()
                }
                ins.velocity = vec
                for (e in ins.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        e.damage(1.0)
                        break
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, 1, 1)


        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}