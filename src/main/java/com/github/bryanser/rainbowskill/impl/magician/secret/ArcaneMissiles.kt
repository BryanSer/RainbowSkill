package com.github.bryanser.rainbowskill.impl.magician.secret

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.github.bryanser.rainbowskill.impl.knighterrant.sword.GaleAndFlyingSword
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object ArcaneMissiles : Skill(
        "奥术飞弹",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 0.0)
        )) {
    override fun onCast(cd: CastData): Boolean {

        val dmg = (getConfigEntry("damage"))(cd).toDouble()
        val player = cd.caster
        val fire: ItemStack = ItemStack(Material.FIRE)

        val ins1 = player.world.spawn(SkillUtils.getLoc(player, false), ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = fire
        }
        val ins2 = player.world.spawn(player.location, ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = fire
        }
        val ins3 = player.world.spawn(SkillUtils.getLoc(player, true), ArmorStand::class.java) {
            it.isVisible = false
            it.itemInHand = fire
        }
        val vec = player.location.direction.normalize()
        object : BukkitRunnable() {
            var time = 0
            override fun run() {
                if (time++ >= 240) {
                    this.cancel()
                }
                ins1.velocity = vec
                SkillUtils.isDamage(ins1, cd, dmg)
                ins2.velocity = vec
                SkillUtils.isDamage(ins2, cd, dmg)
                ins3.velocity = vec
                SkillUtils.isDamage(ins3, cd, dmg)
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }
}