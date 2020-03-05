package com.github.bryanser.rainbowskill

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener

abstract class Passive(
        val name: String
) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, Main.Plugin)
    }

    abstract fun init()

    abstract fun startPassive(p: Player)

    abstract fun stopPassive(p: Player)
}