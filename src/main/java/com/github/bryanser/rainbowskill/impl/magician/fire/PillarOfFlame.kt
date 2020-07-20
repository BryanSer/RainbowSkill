package com.github.bryanser.rainbowskill.impl.magician.fire

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.motion.SkillUtils
import com.github.bryanser.rainbowskill.motion.distanceSquared2
import com.github.bryanser.rainbowskill.particle.Particle
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 在10格内选取一个3x3的区域，出现一个火柱，造成伤害，吟唱需要2.5s
 */
object PillarOfFlame : Skill(
        "烈焰火柱",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Radius", 3.0),
                ConfigEntry("MaxRange", 10.0),
                ConfigEntry("ChantTime", 2.5)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = getConfigEntry("Damage")(cd).toDouble()

        val radius = getConfigEntry("Radius")(cd).toDouble()

        val maxRange = getConfigEntry("MaxRange")(cd).toInt()

        val chantTime = getConfigEntry("ChantTime")(cd).toDouble()

        val p = cd.caster

        ImmobilizeManager.newData().also {
            it.timeLength = chantTime
            ImmobilizeManager.addEffectSelf(p, it)
        }

        val flamesColumn = FlamesColumn(dmg, radius, maxRange, 1, 1)

        flamesColumn.cast(cd)

        return true
    }


}

class FlamesColumn(dmg: Double, radius: Double, range: Int, delay: Int, fireTick: Int) {
    var damage: Double = 0.0
    var radius: Double = 0.0
    var range: Int = 0
    var delay: Int = 0
    var fireTick: Int = 0

    lateinit var particle: Particle

    fun cast(ci: CastData) {
        val p = ci.caster
        var b = p.getTargetBlock(mutableSetOf(Material.AIR), range).location
        while (b.block.type == Material.AIR && b.y > 0) {
            b.add(0.0, -1.0, 0.0)
        }
        val dmg = damage
        val center = b.block.location.add(0.5, 1.0, 0.5)
        val delay = delay
        val r = radius
        val fire = fireTick
        object : BukkitRunnable() {
            val damaged = mutableSetOf(p.entityId)
            var time = 0
            override fun run() {
                if (time++ >= delay) {
                    if (time % 2 == 0)
                        Bukkit.getScheduler().runTaskAsynchronously(Main.Plugin) {
                            drawColumn(center.clone(), r, particle)
                        }
                    for (target in getInColumn(center, r)) {
                        if (damaged.contains(target.entityId)) continue
                        damaged += target.entityId
                        SkillUtils.damage(ci, target, dmg)
                        target.fireTicks = fire
                    }
                    if (time >= delay + 10) {
                        this.cancel()
                    }
                    return
                }
                Bukkit.getScheduler().runTaskAsynchronously(Main.Plugin) {
                    drawCircle(center, r, par = particle)
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)
    }

    private companion object {

        private fun getInColumn(center: Location, r: Double, h: Double = 3.0): Collection<LivingEntity> {
            val center = center.clone()
            val list = mutableSetOf<LivingEntity>()
            val search = sqrt(r * r + h * h)
            val cloc = center.clone()
            cloc.y = 0.0
            for (e in center.world.getNearbyEntities(center, search, search, search)) {
                if (e !is LivingEntity) {
                    continue
                }
                val loc = e.location
                loc.y = 0.0
                if (cloc.distanceSquared2(loc) <= r * r) {
                    list += e
                }
            }
            return list
        }

        private fun drawCircle(center: Location, r: Double, fill: Boolean = false, par: Particle?) {
            var st = 0.0
            while (st < Math.PI * 2) {
                st += Math.PI / if (fill) 6 else 9
                val x = cos(st)
                val z = sin(st)
                val loc = center.clone().add(x * r, 0.0, z * r)
                if (par == null)
                    Br.API.ParticleEffect.ParticleEffect.FLAME.display(0.0f, 0f, 0f, 0F, 2, loc, 50.0)
                else par.play(loc)
                //fire.playParticle(loc, 50.0)
                if (fill) {
                    val del = r / 2
                    var r = r - del
                    while (r > 0) {
                        val vec = Vector.getRandom().multiply(0.1)
                        val loc = center.clone().add(x * r, 0.0, z * r)

                        if (par == null)
                            Br.API.ParticleEffect.ParticleEffect.FLAME.display(vec.x.toFloat(), vec.y.toFloat() * 2, vec.z.toFloat(), 0.03F, 7, loc, 50.0)
                        else par.play(loc)
                        r -= del
                    }
                }
            }
        }

        private fun drawColumn(center: Location, r: Double, par: Particle?) {
            for (i in 1..3) {
                center.add(0.0, 1.0, 0.0)
                drawCircle(center, r, true, par)
            }
        }

    }
}