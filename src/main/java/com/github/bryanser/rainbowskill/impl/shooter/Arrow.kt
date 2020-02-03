package com.github.bryanser.rainbowskill.impl.shooter

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

object Arrow {
    fun archery(cd: CastData, material: Material, time: Double, dmg: Double) {
        val player = cd.caster
        val isType: ItemStack = ItemStack(material)

        val arrowAS = player.world.spawn(
                player.location,
                ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = isType
        }

        val vec = player.location.direction.normalize()

        object : BukkitRunnable() {
            var t = 0
            override fun run() {
                if (t++ >= time * 20) {
                    this.cancel()
                    arrowAS.remove()
                    return
                }
                arrowAS.velocity = vec
                for (e in arrowAS.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        SkillUtils.damage(cd, e, dmg)
                        break
                    }
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)
    }
}