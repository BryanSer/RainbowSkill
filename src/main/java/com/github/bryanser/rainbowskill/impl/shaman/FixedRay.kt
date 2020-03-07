package com.github.bryanser.rainbowskill.impl.shaman

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.ImmobilizeManager
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.idleman.BouquetOfTheGodOfFire
import com.github.bryanser.rainbowskill.impl.minister.HolyLightBall
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Color
import org.bukkit.Material

//向前方发射一个黑色粒子，距离10，击中就定身2.5s，并掉血
object FixedRay : Skill("定身射线", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("Distance", 3.0),
                ConfigEntry("Speed", 0.4),
                ConfigEntry("SettlingTime", 2.5)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()
        val settlingTime = getConfigEntry("SettlingTime")(cd).toDouble()

        val speed = (getConfigEntry("Speed"))(cd).toDouble()

        val loc = cd.caster.eyeLocation.add(0.0, -0.5, 0.0)

        Motion.particleLine(cd, loc, Color.YELLOW, dmg, distance, speed) {
            SkillUtils.damage(cd, it, dmg)
            ImmobilizeManager.newData().also { immobilizeData ->
                immobilizeData.timeLength = settlingTime
                ImmobilizeManager.addEffect(cd, it, immobilizeData)
            }
        }

        return true
    }

}