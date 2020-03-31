package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Passive
import com.github.bryanser.rainbowskill.event.SkillCastEvent
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import java.util.*

/**
 * 每次为别人回复，自己也会回复1%的生命值。
 */
object Rejuvenation : Passive("回春") {
    const val KEY = "回春"
    val activing = hashSetOf<UUID>()

    override fun init() {
    }

    /**
     * 回复生命值
     */
    fun reply(p: Player, recoveryQuantity: Double) {
        val dmg = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).value * recoveryQuantity
        SkillUtils.damage(CastData(p, 1), p, dmg)
    }

    override fun startPassive(p: Player) {
        Corruption.activing.add(p.uniqueId)
    }

    override fun stopPassive(p: Player) {
        Corruption.activing.remove(p.uniqueId)
    }

}