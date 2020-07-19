package com.github.bryanser.rainbowskill.impl.shaman

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.magician.fire.PillarOfFlame
import org.bukkit.Material

/**
 * 技能4：陨星术
 * 在25格内选区一个4x4的范围，然后会落下一堆黑色红色粒子在那个区域，区域内的敌人会掉血，并减速3s，失明3s
 */
object Meteorology : Skill(
        "陨星术",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Range", 2.0),
                ConfigEntry("Time", 2.5)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = getConfigEntry("Damage")(cd).toDouble()
        val range = getConfigEntry("Range")(cd).toDouble()
        val chantTime =getConfigEntry("ChantTime")(cd).toDouble()

        val p = cd.caster

        val target = p.getTargetBlock(mutableSetOf(Material.AIR), 50).location.direction


        return true
    }
}