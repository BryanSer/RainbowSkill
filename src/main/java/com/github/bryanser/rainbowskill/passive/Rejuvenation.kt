package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Passive
import com.github.bryanser.rainbowskill.event.SkillCastEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import java.util.*

/**
 * 每次为别人回复，自己也会回复1%的生命值。
 */
object Rejuvenation : Passive("回春") {
    const val KEY = "回春"
    val activing = hashSetOf<UUID>()

    val range = ConfigEntry("range", 3.0)
    val time = ConfigEntry("time", 2.0)

    override fun init() {
    }

    @EventHandler
    fun onCast(evt: SkillCastEvent) {
        val t = (Alacrity.activing[evt.castData.caster.uniqueId] ?: return)
        val p = evt.player

    }

    override fun startPassive(p: Player) {
        Corruption.activing.add(p.uniqueId)
    }

    override fun stopPassive(p: Player) {
        Corruption.activing.remove(p.uniqueId)
    }

}