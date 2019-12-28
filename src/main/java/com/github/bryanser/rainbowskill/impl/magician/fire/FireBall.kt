package com.github.bryanser.rainbowskill.impl.magician.fire

import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class FireBall : Skill("火球术", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        val fire = SkillUtils.getArmorStand(player, player.location, Material.FIRE, true)
        object : BukkitRunnable() {
            var time = 0
            val vec = player.location.direction.normalize()

            override fun run() {
                if (time++ >= 600) {
                    this.cancel()
                }
                fire.velocity = vec
                for (e in fire.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        e.damage(1.0)
                        this.cancel()
                        break
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}