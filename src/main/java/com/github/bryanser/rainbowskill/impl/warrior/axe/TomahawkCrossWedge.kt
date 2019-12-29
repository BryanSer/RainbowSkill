package com.github.bryanser.rainbowskill.impl.warrior.axe

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import org.bukkit.Material

//对前方长5，宽3造成伤害，蓄力1.5s
object TomahawkCrossWedge : Skill(
        "战斧横劈",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()

        val enemyList = SkillUtils.rangeAttack(cd, 5.0, 3.0)
        enemyList.forEach {
            SkillUtils.damage(cd, it, dmg)
        }
        return true
    }

}