package com.github.bryanser.rainbowskill.impl.magician.secret

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.motion.SkillUtils
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

//技能1：奥术飞弹
//向前方发射三个蓝色粒子，每个只能击中一个单位，造成伤害
object ArcaneMissiles : Skill(
        "奥术飞弹",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 0.0),
                ConfigEntry("Distance", 30.0),
                ConfigEntry("Speed", 0.4)
        )) {
    override fun onCast(cd: CastData): Boolean {

        val dmg = getConfigEntry("Damage")(cd).toDouble()

        val distance = getConfigEntry("Distance")(cd).toDouble()
        val speed = getConfigEntry("Speed")(cd).toDouble()


        val player = cd.caster
        val loc = player.location
        val loc1 = SkillUtils.getLoc(player, true)
        val loc2 = SkillUtils.getLoc(player, false)

        val vector = loc.direction.normalize()

        particleEmission(dmg, distance, vector, loc, speed, cd)
        particleEmission(dmg, distance, vector, loc1, speed, cd)
        particleEmission(dmg, distance, vector, loc2, speed, cd)

        return true
    }

    private fun particleEmission(dmg: Double, distance: Double, vector: Vector, loc: Location, speed: Double, cd: CastData) {
        object : BukkitRunnable() {
            var p = distance

            val player = cd.caster

            override fun run() {
                if (p <= 0) {
                    this.cancel()
                    return
                }
                val t = vector.clone().multiply(distance - p)
                ParticleEffect.REDSTONE.display(
                        ParticleEffect.OrdinaryColor(Color.BLUE),
                        loc.add(t), 50.0)

                p -= speed
                for (e in loc.world.getNearbyEntities(loc, 0.1, 0.1, 0.1)) {
                    if (e is LivingEntity && e != player) {
                        this.cancel()
                        SkillUtils.damage(cd, e, dmg)
                        SpeedManager.newData().also {
                            it.modifier = -0.1
                            it.timeLength = 1.0
                            SpeedManager.addEffect(cd, e, it)
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.Plugin, 0, 1)
    }
}