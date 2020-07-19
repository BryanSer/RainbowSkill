package com.github.bryanser.rainbowskill.motion

import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.AttributeManager
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.impl.magician.fire.FlameCurse
import com.github.bryanser.rainbowskill.passive.Uncertain
import com.github.bryanser.rainbowskill.passive.Uncertain.remove
import com.github.bryanser.rainbowskill.passive.removeAttribute
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.scheduler.BukkitRunnable

object StatusIncrease {
    fun statusIncrease(KEY: String, type: String, cd: CastData, increase: Double, durationTime: Double) {
        val p = cd.caster

        when (type) {
            "attack" -> {
                val attr = p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
                attr.remove()
                attr.addModifier(AttributeModifier(KEY, increase, AttributeModifier.Operation.MULTIPLY_SCALAR_1))
            }
            "speed" -> {
                val attr = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                attr.remove()
                attr.addModifier(AttributeModifier(KEY, increase, AttributeModifier.Operation.MULTIPLY_SCALAR_1))
            }
            "defense" -> {
                val attr = p.getAttribute(Attribute.GENERIC_ARMOR)
                attr.remove()
                attr.addModifier(AttributeModifier(KEY, increase, AttributeModifier.Operation.MULTIPLY_SCALAR_1))
            }
        }

        object : BukkitRunnable() {
            override fun run() {
                when (type) {
                    "attack" -> {
                        p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).remove()
                    }
                    "speed" -> {
                        p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).remove()
                    }
                }

            }
        }.runTaskLater(Main.getPlugin(), (durationTime * 20).toLong())
    }


}