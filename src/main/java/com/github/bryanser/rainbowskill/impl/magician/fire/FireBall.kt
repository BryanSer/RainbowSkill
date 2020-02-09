package com.github.bryanser.rainbowskill.impl.magician.fire

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

//向前方发射一个火球，击中后发生小爆炸，长度是30，需要吟唱1s
object FireBall : Skill(
        "火球术",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Long", 30.0),
                ConfigEntry("Time", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val time = (getConfigEntry("Time"))(cd).toLong()
        val l = (getConfigEntry("Long"))(cd).toDouble()

        val player = cd.caster
        val fire = SkillUtils.getArmorStand(player, player.location, Material.FIREBALL, false)

        //it.addPotionEffect(PotionEffect((PotionEffectType.BLINDNESS), 300, -3))
        SpeedManager.newData().also {
            it.modifier = -1.0
            it.timeLength = 3.0
            SpeedManager.addEffect(player, it)
        }

        object : BukkitRunnable() {
            val vec = player.location.direction.normalize()

            val loc = player.location

            val ll = l * l

            override fun run() {

                //val d = SkillUtils.getDistance(loc, fire.location)
                val d = loc.distanceSquared(fire.location)
                if (d >= ll) {
                    fire.remove()
                    this.cancel()
                    return
                }
                fire.velocity = vec
                for (e in fire.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        SkillUtils.damage(cd, e, dmg)
                        e.world.createExplosion(e.location, 0.0F)
                        this.cancel()
                        break
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, 20 * time, 1)
        return true
    }



}