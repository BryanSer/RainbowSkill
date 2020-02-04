package com.github.bryanser.rainbowskill.impl.minister

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.BuffZone
import com.github.bryanser.rainbowskill.motion.Motion
import org.bukkit.Material

//技能4：大圣光治疗术
//给半径2以内的队友恢复生命值，其恢复值就是这个技能的自定义伤害
object GreatLightTherapy : Skill("大圣光治疗术", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("Range", 2.0),
                ConfigEntry("Time", 3.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val range = getConfigEntry("Range")(cd).toDouble()
        val dmg = getConfigEntry("Damage")(cd).toDouble()
        val time = getConfigEntry("Time")(cd).toInt()

        //BuffZone.castSelfBuffZone(cd,time,)

        return true
    }

}