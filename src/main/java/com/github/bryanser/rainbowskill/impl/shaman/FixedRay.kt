package com.github.bryanser.rainbowskill.impl.shaman

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.idleman.BouquetOfTheGodOfFire
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Material

//向前方发射一个黑色粒子，距离10，击中就定身2.5s，并掉血
object FixedRay : Skill("定身射线", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("Distance", 3.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()
        val player = cd.caster
        val loc = player.location

        var amount = 1
        var offsetx = 0f
        var offsety = 0f
        var offsetz = 0f
        var speed = 0.25f

        ParticleEffect.FLAME.display(offsetx, offsety, offsetz, speed, amount, loc, 50.0)

        return true
    }

}