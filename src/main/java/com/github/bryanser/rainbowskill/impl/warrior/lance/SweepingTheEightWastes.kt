package com.github.bryanser.rainbowskill.impl.warrior.lance

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.*

object SweepingTheEightWastes : Skill("横扫八荒", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster
        for (e in player.getNearbyEntities(2.0, 1.0, 2.0)) {
            if (e == player) {
                continue
            } else if (e is LivingEntity) {
                e.damage(1.0)
            }
        }
        return true
    }

}