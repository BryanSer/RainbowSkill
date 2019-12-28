package com.github.bryanser.rainbowskill.impl.shooter.knighterrant

import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
//对前方发射一根有红色轨迹的箭，
// 飞行长度是30，击中实体或方块后发生一次中爆炸并弹开范围内的敌人
class BlowoutArrow : Skill("爆裂箭", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        val arrow = SkillUtils.getArmorStand(player, player.location, Material.ARROW, false)
        val vec = player.location.direction.normalize()
        object : BukkitRunnable() {
            var time = 0
            override fun run() {
                if (time++ >= 600) {
                    this.cancel()
                }
                arrow.velocity = vec
                for (e in arrow.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        e.damage(1.0)
                        break
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}