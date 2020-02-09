package com.github.bryanser.rainbowskill.event

import com.github.bryanser.rainbowskill.CastData
import com.relatev.minecraft.RainbowHero.skill.Castable
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class SkillCastEvent(
        val player: Player,
        val skill: Castable,
        val castData: CastData
) : Event(), Cancellable {
    var cancel = false

    override fun getHandlers(): HandlerList = list

    companion object {
        val list = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList = list
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }

    override fun isCancelled(): Boolean = cancel
}