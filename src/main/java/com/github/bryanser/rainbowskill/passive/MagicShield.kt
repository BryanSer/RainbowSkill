package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Passive
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

object MagicShield: Passive("秘法盾"){
    val activing = hashMapOf<UUID,Long>()

    val cooldown = ConfigEntry("Cooldown", 120 * 1000.0)
    val health = ConfigEntry("Health", 0.1)
    val recover = ConfigEntry("Recover", 0.15)
    val speedTime = ConfigEntry("SpeedTime", 30.0)

    override fun init() {
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    fun onDamage(evt: EntityDamageEvent){
        val p = evt.entity as? Player ?: return
        val last = activing[p.uniqueId] ?: return
        val pass = System.currentTimeMillis() - last
        if(pass > cooldown(p).toLong()){
            val h = p.health - evt.finalDamage
            val max = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).value
            if(h / max < health(p).toDouble()){
                val r = max * recover(p).toDouble()
                var t = h + r
                if(t > max) t = max
                p.health = t
                p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, speedTime(p).toInt(), 1))
                activing[p.uniqueId] = System.currentTimeMillis()
            }
        }
    }

    override fun startPassive(p: Player) {
        activing[p.uniqueId] = 0L
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }

}