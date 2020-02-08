package com.github.bryanser.rainbowskill.impl.shooter.elf

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.shooter.ArrowPenetrate
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material

//对前方射出一根箭，造成伤害，飞行长度是20，击中实体后能穿透
object PiercingArrow : Skill(
        "穿透箭",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 20.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val distance = getConfigEntry("Distance")(cd).toDouble()
        val dmg = getConfigEntry("Damage")(cd).toDouble()
        val loc = cd.caster.location
        val vec = loc.direction.normalize()

        ArrowPenetrate.cast(cd, Material.ARROW, loc, vec, distance, true) {
            SkillUtils.damage(cd, it, dmg)
        }
        return true
    }
}