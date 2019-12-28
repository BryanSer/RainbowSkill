package com.github.bryanser.rainbowskill.impl.shooter.hunter

import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.Motion
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class PostJumpEjection : Skill("后跳弹射", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        Motion.jump(player, -1,3.0)

        val arrow = SkillUtils.getArmorStand(player, player.location, Material.ARROW, false)
        val vec = player.location.direction.normalize()
        object : BukkitRunnable() {
            var time = 0
            override fun run() {
                if (time++ >= 300) {
                    this.cancel()
                }
                arrow.velocity = vec
                for (e in arrow.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        break
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}