package com.github.bryanser.rainbowskill.impl.shooter.hunter

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.impl.shooter.ArrowPenetrate
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material

//射出一支能飞30格远的箭，被射中就定身2s
object MucusArrow : Skill(
        "粘液箭",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance",30.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val distance = (getConfigEntry("Distance"))(cd).toDouble()
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val loc = cd.caster.location
        val vec = loc.direction.normalize()

        ArrowPenetrate.cast(cd, Material.ARROW, loc, vec, distance, true) {
            SkillUtils.damage(cd, it, dmg)
            ImmobilizeManager.newData().also {data->
                data.modifier = -0.1
                data.timeLength = 2.0
                ImmobilizeManager.addEffect(cd, it,data)
            }
        }
        return true
    }

}