package com.github.bryanser.rainbowskill.impl.shooter.elf

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.shooter.ArrowPenetrate
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material

//对前方扇形射出三根箭，每根伤害一样，不可以穿透，飞行距离是10
object Scattering : Skill(
        "散射",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 10.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val distance = getConfigEntry("Distance")(cd).toDouble()
        val dmg = getConfigEntry("Damage")(cd).toDouble()
        val player = cd.caster

        val vec = player.location.direction.normalize()

        val centerLoc = player.location
        val leftLoc = SkillUtils.getLoc(player, true)
        val rightLoc = SkillUtils.getLoc(player, false)

        val leftVec = vec.clone().add(Utils.getLeft(vec).multiply(0.50))
        val rightVec = vec.clone().add(Utils.getRight(vec).multiply(0.50))

        ArrowPenetrate.cast(cd, Material.ARROW, centerLoc, vec, distance, false) {
            SkillUtils.damage(cd, it, dmg)
        }
        ArrowPenetrate.cast(cd, Material.ARROW, leftLoc, leftVec, distance, false) {
            SkillUtils.damage(cd, it, dmg)
        }
        ArrowPenetrate.cast(cd, Material.ARROW, rightLoc, rightVec, distance, false) {
            SkillUtils.damage(cd, it, dmg)
        }
        return true
    }
}