package com.github.bryanser.rainbowskill.impl.magician.secret

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

//技能1：奥术飞弹
//向前方发射三个蓝色粒子，每个只能击中一个单位，造成伤害
object ArcaneMissiles : Skill(
        "奥术飞弹",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 0.0)
        )) {
    override fun onCast(cd: CastData): Boolean {

        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val player = cd.caster
        val fire: ItemStack = ItemStack(Material.FIRE)

        val ins1 = ArmorStandManager.createArmorStand(SkillUtils.getLoc(player, false)) {
            it.isVisible = false
            it.itemInHand = fire
        }
        val ins2 = ArmorStandManager.createArmorStand(player.location) {
            it.isVisible = false
            it.itemInHand = fire
        }
        val ins3 = ArmorStandManager.createArmorStand(SkillUtils.getLoc(player, true)) {
            it.isVisible = false
            it.itemInHand = fire
        }
        val vec = player.location.direction.normalize()
        object : BukkitRunnable() {
            var time = 0
            override fun run() {
                if (time++ >= 240) {
                    ins1.remove()
                    ins2.remove()
                    ins3.remove()
                    this.cancel()
                    return
                }
                ins1.velocity = vec
                SkillUtils.isDamage(ins1, cd, dmg, false)
                ins2.velocity = vec
                SkillUtils.isDamage(ins2, cd, dmg, false)
                ins3.velocity = vec
                SkillUtils.isDamage(ins3, cd, dmg, false)
            }
        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }
}