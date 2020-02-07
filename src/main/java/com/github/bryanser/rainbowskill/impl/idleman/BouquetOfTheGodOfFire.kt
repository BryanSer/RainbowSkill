package com.github.bryanser.rainbowskill.impl.idleman

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

//发射一个火焰粒子，飞行距离为15，被击中会被减速10%,持续1s
object BouquetOfTheGodOfFire : Skill("火神的花束", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("Distance", 15.0),
                ConfigEntry("Speed", 0.4)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()
        val speed = (getConfigEntry("Speed"))(cd).toDouble()
        val player = cd.caster
        val loc = player.eyeLocation.add(0.0, -0.5, 0.0)

        val vector = loc.direction.normalize()

        object : BukkitRunnable() {
            var p = distance
            val vec = vector
            val damaged = hashSetOf<Int>()
            override fun run() {
                if (p <= 0) {
                    this.cancel()
                    return;
                }
                val t = vec.clone().multiply(distance - p)
                val loc = loc.clone().add(t)
                ParticleEffect.FLAME.display(
                        0f,
                        0f,
                        0f,
                        0.0f, 3, loc, 50.0)
                p -= speed
                for (e in loc.world.getNearbyEntities(loc, 0.1, 0.1, 0.1)) {
                    if (e is LivingEntity && e != player && e.entityId !in damaged) {
                        damaged += e.entityId
                        SkillUtils.damage(cd, e, dmg)
                        SpeedManager.newData().also {
                            it.modifier = -0.1
                            it.timeLength = 1.0
                            SpeedManager.addEffect(e, it)
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.Plugin, 0, 1)


        return true
    }

}