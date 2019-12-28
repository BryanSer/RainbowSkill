package com.github.bryanser.rainbowskill.impl.magician.thunder

import com.github.bryanser.rainbowskill.Skill
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.*
//对半径3以内的敌人造成伤害，并减速3s，沉默1s，并立刻劈下5道雷在身边
class AnythingThatVanishesInAFlash : Skill("电光石火", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {

        for (e in player.getNearbyEntities(3.0, 1.0, 3.0)) {
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