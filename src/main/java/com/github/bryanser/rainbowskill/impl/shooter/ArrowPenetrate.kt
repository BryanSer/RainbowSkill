package com.github.bryanser.rainbowskill.impl.shooter

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.motion.ArmorStandManager
import com.github.bryanser.rainbowskill.motion.distanceSquared2
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.EulerAngle
import java.util.*
import kotlin.math.PI

object ArrowHitEffect : Listener, BukkitRunnable() {
    init {
        Bukkit.getPluginManager().registerEvents(this, Main.Plugin)
        this.runTaskTimer(Main.Plugin, 10, 10)
    }

    override fun run() {
        val it = casting.iterator()
        while (it.hasNext()) {
            val (p, callback, td) = it.next().value
            if (p.location.distanceSquared2(td.first) >= td.second) {
                it.remove()
                callback(null, null)
                p.remove()
            }
        }
    }

    val casting = hashMapOf<UUID, Triple<out Projectile, (Location?, Entity?) -> Unit, Pair<Location, Double>>>()
    const val METADATA_KEY = "rainbowskill_arrow_hit_effect"
    fun <T : Projectile> cast(cd: CastData,
                              type: Class<T>,
                              loc: Location,
                              maxDistance: Double,
                              vector: org.bukkit.util.Vector,
                              onHit: (Location?, Entity?) -> Unit): T {
        val player = cd.caster
        val tloc = loc.clone()
        tloc.direction = vector
        val t = player.launchProjectile(type, vector)
        t.setGravity(false)
        t.setMetadata(METADATA_KEY, FixedMetadataValue(Main.Plugin, cd.castId.toString()))
        casting[cd.castId] = Triple(t, onHit, loc.clone() to maxDistance * maxDistance)
        return t
    }

    @EventHandler
    fun onHit(evt: ProjectileHitEvent) {
        val t = evt.entity
        if (!t.hasMetadata(METADATA_KEY)) {
            return
        }
        val uuid = UUID.fromString(t.getMetadata(METADATA_KEY)[0].asString())
        val (cd, callback) = casting.remove(uuid) ?: return
        if (evt.hitBlock != null) {
            callback(evt.hitBlock.location, null)
        } else if (evt.hitEntity != null) {
            callback(null, evt.hitEntity)
        }
    }


}

object ArrowPenetrate {

    fun cast(cd: CastData,
             material: Material,
             loc: Location,
             vector: org.bukkit.util.Vector,
             distance: Double,
             penetrate: Boolean,
             flying: (ArmorStand) -> Unit = {},
             effect: (LivingEntity) -> Unit) {
        val player = cd.caster
        val isType: ItemStack = ItemStack(material)
        val tloc = loc.clone()
        tloc.direction = vector

        val arrowAS = ArmorStandManager.createArmorStand(
                tloc) {
            it.isVisible = false
            it.isMarker = true
            it.itemInHand = isType
            it.rightArmPose = EulerAngle(0.0, -PI / 4, 0.0)
        }

        archery(cd, loc, vector, arrowAS, distance, penetrate, flying, effect)
    }

    private fun archery(cd: CastData,
                        loc: Location,
                        vector: org.bukkit.util.Vector,
                        armorStand: ArmorStand,
                        distance: Double,
                        penetrate: Boolean,
                        flying: (ArmorStand) -> Unit,
                        effect: (LivingEntity) -> Unit) {
        object : BukkitRunnable() {
            val dis2 = distance * distance

            init {
                run()
            }

            override fun run() {
                val dis = loc.distanceSquared2(armorStand.location)
                if (dis >= dis2) {
                    this.cancel()
                    armorStand.remove()
                    return
                }
                armorStand.velocity = vector
                flying(armorStand)
                for (e in armorStand.getNearbyEntities(0.1, 0.2, 0.1)) {
                    if (e == cd.caster) {
                        continue
                    } else if (e is LivingEntity) {
                        effect(e)
                        if (!penetrate) {
                            armorStand.remove()
                        }
                        break
                    }
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)
    }
}