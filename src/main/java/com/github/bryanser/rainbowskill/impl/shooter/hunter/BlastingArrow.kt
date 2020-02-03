package com.github.bryanser.rainbowskill.impl.shooter.hunter

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

//射出一支飞行20格的箭，碰撞了实体或方块后发生小爆炸，
// 接下来的五秒内再发生三次爆炸，每次都能弹开敌人并造成伤害（自定义）
object BlastingArrow : Skill(
        "爆破箭",
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
                        break
                    }
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }

}