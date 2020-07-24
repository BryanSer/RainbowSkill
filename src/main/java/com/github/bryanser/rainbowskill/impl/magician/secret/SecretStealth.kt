package com.github.bryanser.rainbowskill.impl.magician.secret

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.SpeedManager
import com.github.bryanser.rainbowskill.motion.Motion
import org.bukkit.Material

//技能3：奥秘隐身
//隐身3秒，加速度50%3秒
object SecretStealth : Skill(
        "奥秘隐身",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0)
        )) {
    override fun onCast(cd: CastData): Boolean {


        SpeedManager.newData().also { speedData ->
            speedData.modifier = 0.5
            speedData.timeLength = 3.0
            SpeedManager.addEffect(cd, cd.caster, speedData)
        }
        return true
    }

}