package com.github.bryanser.rainbowskill.impl.knighterrant.sword

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable


// 用这个技能物品左键击中一名敌人后，会陆续掉下三把剑造成三次伤害，
// 在技能自定义中，自定义的是每次造成的伤害的值，而不是三次的总额
object SwiftWindShadowlessSword : Skill(
        "疾风无影剑",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("FDamage", 1.0),
                ConfigEntry("SDamage", 1.0),
                ConfigEntry("TDamage", 1.0),
                ConfigEntry("Time", 4.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val firstDmg = (getConfigEntry("FDamage"))(cd).toDouble()
        val secondDmg = (getConfigEntry("SDamage"))(cd).toDouble()
        val thirdDmg = (getConfigEntry("TDamage"))(cd).toDouble()

        var dmg = mutableListOf<Double>()
        dmg.add(firstDmg)
        dmg.add(secondDmg)
        dmg.add(thirdDmg)

        val player = cd.caster
        val item = player.itemInHand



        return true
    }

    fun dropAttack(cd: CastData,
                   enemy: LivingEntity,
                   material: Material,
                   dmg: MutableList<Double>) {
        val itemStack: ItemStack = ItemStack(material)

        val times = 1.0

        var index: Long = 1
        dmg.forEach { dmg ->
            drop(cd, enemy, itemStack, dmg, times, index)
            index++
        }
    }

    fun drop(cd: CastData,
               enemy: LivingEntity,
               itemStack: ItemStack,
               dmg: Double,
               times: Double,
               start: Long) {
        object : BukkitRunnable() {
            var time = 0
            val loc = enemy.location

            val ins = enemy.world.spawn(
                    loc.add(0.0, 1.0, 0.0),
                    ArmorStand::class.java) {
                it.isMarker = false
                it.setGravity(true)
//                    it.isVisible = false
                it.itemInHand = itemStack
            }

            override fun run() {
                if (time++ >= times * 20) {
                    ins.remove()
                    this.cancel()
                    return
                }
                for (e in ins.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == cd.caster) {
                        continue
                    } else if (e == enemy) {
                        SkillUtils.damage(cd, enemy, dmg)
                        break
                    }
                }
            }
        }.runTaskTimer(Main.Plugin, start, 1)
    }

    @EventHandler
    fun onPlayerHit(event: EntityDamageByEntityEvent) {
        if (event.entityType == EntityType.ENDER_CRYSTAL) {
            if (event.damager is Player) {

            }
        }
    }

}