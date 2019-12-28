package com.github.bryanser.rainbowskill.impl.shooter.knighterrant

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
//射出一根能飞行40格的箭，后面跟着红色粒子，
// 碰撞了方块或者实体后发生小爆炸，炸开敌人，不造成伤害
object ExplosiveFire : Skill("爆炸射击", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("damage"))(cd).toDouble()
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
                if (time++ >= 800) {
                    this.cancel()
                }
                arrowAS.velocity = vec
                for (e in arrowAS.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        break
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }

}