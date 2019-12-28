package com.github.bryanser.rainbowskill.impl.magician.secret

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import org.bukkit.Material
object CurseOfMystery : Skill(
        "奥秘诅咒",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 0.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        return true
    }

}