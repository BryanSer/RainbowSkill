package com.github.bryanser.rainbowskill.impl.warrior.lance

import com.github.bryanser.rainbowskill.Skill
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.*

class SweepingTheEightWastes : Skill("横扫八荒", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {

        for (e in player.getNearbyEntities(2.0, 1.0, 2.0)) {
            if (e == player) {
                continue
            } else if (e is LivingEntity) {
                e.damage(1.0)
            }
        }

        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}