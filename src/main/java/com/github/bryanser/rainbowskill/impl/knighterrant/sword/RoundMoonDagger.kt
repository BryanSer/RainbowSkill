package com.github.bryanser.rainbowskill.impl.knighterrant.sword

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.ArmorStandManager
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

//丢出三把匕首，攻击范围宽3，长5,这些匕首无法穿透敌人，击中了敌人后就会消失
object RoundMoonDagger : Skill("圆月匕首", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Time", 4.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster
        val ironSword: ItemStack = ItemStack(Material.IRON_SWORD)
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val time = (getConfigEntry("Time"))(cd).toDouble()

        val sas1 = ArmorStandManager.createArmorStand(SkillUtils.getLoc(player, true)) {
            it.isVisible = true
            it.itemInHand = ironSword
        }
        val sas2 = ArmorStandManager.createArmorStand(player.location) {
            it.isVisible = true
            it.itemInHand = ironSword
        }
        val sas3 = ArmorStandManager.createArmorStand(SkillUtils.getLoc(player, false)) {
            it.isVisible = true
            it.itemInHand = ironSword
        }

        val vec = player.location.direction.normalize()
        object : BukkitRunnable() {
            var t = 0

            override fun run() {
                if (t++ >= time * 20) {
                    sas1.remove()
                    sas2.remove()
                    sas3.remove()
                    this.cancel()
                    return
                }
                sas1.velocity = vec
                SkillUtils.isDamage(sas1, cd, dmg, false)
                sas2.velocity = vec
                SkillUtils.isDamage(sas2, cd, dmg, false)
                sas3.velocity = vec
                SkillUtils.isDamage(sas3, cd, dmg, false)
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }

}