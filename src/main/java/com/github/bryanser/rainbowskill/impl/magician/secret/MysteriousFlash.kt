package com.github.bryanser.rainbowskill.impl.magician.secret

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import org.bukkit.Material


//向前方5格闪现
object MysteriousFlash : Skill(
        "奥秘闪现",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        Motion.flash(cd.caster, 1, 5.0)
        return true
    }

}