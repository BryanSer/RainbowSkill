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
                ConfigEntry("Distance", 30.0),
                ConfigEntry("StorageTime", 3.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()
        val storageTime = getConfigEntry("StorageTime")(cd).toDouble()

        val player = cd.caster
        val fire = SkillUtils.getArmorStand(player, player.location, Material.FIREBALL, false)

        //it.addPotionEffect(PotionEffect((PotionEffectType.BLINDNESS), 300, -3))
        ImmobilizeManager.newData().also {
            it.modifier = -1.0
            it.timeLength = storageTime
            ImmobilizeManager.addEffectSelf(player, it)
        }

        object : BukkitRunnable() {
            val vec = player.location.direction.normalize()

            val loc = player.location

            val ll = distance * distance

            override fun run() {

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
                        val t = e.location
                        t.world.createExplosion(t.x, t.y, t.z, 0.0F, false, false)
                        this.cancel()
                        break
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, (20 * storageTime).toLong(), 1)
        return true
    }


}