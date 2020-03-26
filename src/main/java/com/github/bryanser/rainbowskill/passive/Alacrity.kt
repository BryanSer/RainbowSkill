package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Passive
import com.github.bryanser.rainbowskill.event.SkillCastEvent
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import java.util.*

object Alacrity : Passive("轻敏") {
    const val KEY = "轻敏"
    val activing = hashMapOf<UUID, Pair<Int, Long>>()
    val effect = ConfigEntry("Effect", 0.06)
    val time = ConfigEntry("Time", 2000.0)
    val max = ConfigEntry("Max", 0.18)

    override fun init() {
        Bukkit.getScheduler().runTaskTimer(Main.Plugin, {
            for ((uid, t) in activing) {
                val pass = System.currentTimeMillis() - t.second
                val p = Bukkit.getPlayer(uid) ?: continue
                val cd = CastData(p, 1)
                if (pass < time(cd).toDouble()) {
                    var eff = effect(cd).toDouble() * t.first
                    val max = max(cd).toDouble()
                    if (eff > max) {
                        eff = max
                    }
                    val attr = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                    attr.modifiers.iterator().run {
                        while (hasNext()) {
                            val n = next()
                            if (n.name == KEY) {
                                remove()
                            }
                        }
                    }
                    attr.addModifier(org.bukkit.attribute.AttributeModifier(
                            KEY, eff, org.bukkit.attribute.AttributeModifier.Operation.ADD_SCALAR
                    ))
                }else{
                    val attr = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                    attr.modifiers.iterator().run {
                        while (hasNext()) {
                            val n = next()
                            if (n.name == KEY) {
                                remove()
                            }
                        }
                    }
                }
            }
        }, 5, 5)
    }

    @EventHandler
    fun onCast(evt: SkillCastEvent) {
        val t = (activing[evt.castData.caster.uniqueId] ?: return)
        val pass = System.currentTimeMillis() - t.second
        val p = evt.player
        if (pass > time(evt.castData).toDouble()) {
            activing[p.uniqueId] = 1 to System.currentTimeMillis()
        } else {
            activing[p.uniqueId] = t.first + 1 to System.currentTimeMillis()
        }
    }

    override fun startPassive(p: Player) {
        activing[p.uniqueId] = 0 to 0L
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }

}