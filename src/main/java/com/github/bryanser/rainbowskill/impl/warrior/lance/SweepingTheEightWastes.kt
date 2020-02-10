package com.github.bryanser.rainbowskill.impl.warrior.lance

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material
import org.bukkit.entity.LivingEntity

//技能3：横扫八荒
//对半径2以内的敌人造成伤害并击退，蓄力2s
object SweepingTheEightWastes : Skill("横扫八荒", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {

        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()

        val player = cd.caster
        for (e in player.getNearbyEntities(2.0, 1.0, 2.0)) {
            if (e == player) {
                continue
            } else if (e is LivingEntity) {
                SkillUtils.damage(cd,e,dmg)
                Motion.knock(cd, e, distance)
            }
        }
        return true
    }

}