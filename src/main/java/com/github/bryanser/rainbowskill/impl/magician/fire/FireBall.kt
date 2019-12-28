package com.github.bryanser.rainbowskill.impl.magician.fire

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object FireBall : Skill(
        "火球术",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("damage"))(cd).toDouble()
        val time = (getConfigEntry("time"))(cd).toDouble()

        val player = cd.caster
        val fire = SkillUtils.getArmorStand(player, player.location, Material.FIRE, true)
        object : BukkitRunnable() {
            var t = 0
            val vec = player.location.direction.normalize()

            override fun run() {
                if (t++ >= 600) {
                    this.cancel()
                }
                fire.velocity = vec
                for (e in fire.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        SkillUtils.damage(cd, e, dmg)
                        this.cancel()
                        break
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }

}