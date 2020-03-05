package com.github.bryanser.rainbowskill.impl.magician.thunder

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.SpeedManager
import org.bukkit.Material

//技能3：风驰电掣
//增加100%的速度，持续5s
object GoByLikeTheWind : Skill("风驰电掣", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Effect", 1.0),
                ConfigEntry("Time",5.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster

        val effect = getConfigEntry("Effect")(cd).toDouble()
        val time = getConfigEntry("Time")(cd).toDouble()

        SpeedManager.newData().also {
            it.modifier = effect
            it.timeLength = time
            SpeedManager.addEffect(cd, player, it)
        }

        return true
    }

}