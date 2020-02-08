package com.github.bryanser.rainbowskill.impl.shooter.hunter

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.shooter.ArrowPenetrate
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material

//往后跳3格远，并快速向前方射出一根箭（飞行15格远），箭的伤害=普攻伤害
object PostJumpEjection : Skill("后跳弹射",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance",15.0)
        )) {
    override fun onCast(cd: CastData): Boolean {

        val distance = getConfigEntry("Distance")(cd).toDouble()
        val dmg = getConfigEntry("Damage")(cd).toDouble()

        val player = cd.caster
        Motion.flash(player, -1, 3.0)

        val loc = cd.caster.location
        val vec = loc.direction.normalize()

        ArrowPenetrate.cast(cd, Material.ARROW, loc, vec, distance, true) {
            SkillUtils.damage(cd, it, dmg)
        }
        return true
    }

}