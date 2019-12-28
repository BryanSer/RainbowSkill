package com.github.bryanser.rainbowskill.impl.knighterrant.sword

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import org.bukkit.Material

object SwiftWindShadowlessSword : Skill(
        "疾风无影剑",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("time", 4.0)
        )) {
    override fun onCast(cd: CastData): Boolean {

        return true
    }

}