package com.github.bryanser.rainbowskill.impl.assassin.dagger

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.Motion
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*


//向前方冲刺5格，对范围内所有敌人造成伤害的同时，还让他们失明减速3s
object DeciduousLeafCutting : Skill("落叶斩", mutableListOf(""), Material.REDSTONE, listOf(
        ConfigEntry(COOLDOWN_KEY, 10.0),
        ConfigEntry("Damage", 10.0)
)) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("damage"))(cd).toDouble()
        Motion.charge(cd.caster, dmg, 5.0)

        return true
    }

}