package com.github.bryanser.rainbowskill.impl.assassin.dagger

import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.Motion
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*


//向前方冲刺5格，对范围内所有敌人造成伤害的同时，还让他们失明减速3s
class DeciduousLeafCutting : Skill("落叶斩", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {

        Motion.charge(player,1.0,5.0)

        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}