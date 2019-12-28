package com.github.bryanser.rainbowskill.impl.warrior.axe

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

object Rage : Skill("狂怒", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        return true
    }

}