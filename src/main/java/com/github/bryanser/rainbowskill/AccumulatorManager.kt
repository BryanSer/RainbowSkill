package com.github.bryanser.rainbowskill

import com.github.bryanser.rainbowskill.event.SkillCastEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object AccumulatorManager : Listener {
    val casting = mutableListOf<Int>()



    init {
        Bukkit.getPluginManager().registerEvents(this, Main.Plugin)
    }

    @EventHandler
    fun onCast(evt: SkillCastEvent) {
    }
}