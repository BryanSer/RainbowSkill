package com.github.bryanser.rainbowskill.impl.shooter.knighterrant

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.shooter.ArrowPenetrate
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material

//对前方发射一根有红色轨迹的箭，
// 飞行长度是30，击中实体或方块后发生一次中爆炸并弹开范围内的敌人
object BlowoutArrow : Skill(
        "爆裂箭",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance",30.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val distance = getConfigEntry("Distance")(cd).toDouble()
        val dmg = getConfigEntry("Damage")(cd).toDouble()
        val loc = cd.caster.location
        val vec = loc.direction.normalize()

        ArrowPenetrate.cast(cd, Material.ARROW, loc, vec, distance, false) {
            SkillUtils.damage(cd, it, dmg)
            it.world.createExplosion(it.location,0.0F)
        }
        return true
    }

}