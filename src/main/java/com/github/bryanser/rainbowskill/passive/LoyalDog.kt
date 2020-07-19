package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Passive
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Wolf
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*


/**
 * 在生命值低于20%的时候，身边出现三只狗，伤害和原版一样，会追击最近的敌人，直到狗狗被杀，否则一直追击
 */
object LoyalDog : Passive("忠犬") {
    const val KEY = "忠犬"
    val activing = hashSetOf<UUID>()

    val health = ConfigEntry("Health", 0.2)
    val range = ConfigEntry("Range", 5.0)

    override fun init() {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onDamage(evt: EntityDamageByEntityEvent) {
        val d = evt.damager as? Player ?: return
        val h = d.health - evt.finalDamage
        val max = d.getAttribute(Attribute.GENERIC_MAX_HEALTH).value
        if (h / max <= health(d).toDouble()) {
            val wolf1 = d.world.spawn(d.location, Wolf::class.java)
            val wolf2 = d.world.spawn(d.location, Wolf::class.java)
            val wolf3 = d.world.spawn(d.location, Wolf::class.java)
            val range = range(d).toDouble()
            var min = range + 1
            var target: LivingEntity? = null
            val loc = d.location
            for (e in d.getNearbyEntities(range, 1.0, range)) {
                if (e == d) {
                    continue
                } else if (e is LivingEntity) {
                    val d = loc.distanceSquared(e.location)
                    if (d < min) {
                        min = d
                        target = e
                    }
                }
            }
            if (target != null) {
                wolf1.target = target
                wolf2.target = target
                wolf3.target = target
            }
        }
    }

    override fun startPassive(p: Player) {
        activing.add(p.uniqueId)
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }

}