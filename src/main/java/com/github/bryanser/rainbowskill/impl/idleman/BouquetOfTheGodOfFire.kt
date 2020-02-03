package com.github.bryanser.rainbowskill.impl.idleman

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Material

//发射一个火焰粒子，飞行距离为15，被击中会被减速10%,持续1s
object BouquetOfTheGodOfFire : Skill("火神的花束", mutableListOf(""), Material.REDSTONE,
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

        val vector = loc.direction.normalize()

        var amount = 1
        var offsetx = 0f
        var offsety = 0f
        var offsetz = 0f
        var speed = 0.25f

        ParticleEffect.FLAME.display(
                vector.x.toFloat(),
                vector.y.toFloat(),
                vector.z.toFloat(),
                speed, amount, loc, 50.0)

        return true
    }

}