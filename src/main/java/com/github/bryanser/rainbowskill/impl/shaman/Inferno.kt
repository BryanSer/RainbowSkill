package com.github.bryanser.rainbowskill.impl.shaman

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Material

//对着自我半径3释放一个圆形的灰色粒子波，敌人会被击退，并掉血
object Inferno : Skill("地狱烈焰", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("Distance", 3.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()

        return true
    }

}