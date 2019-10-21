package com.github.bryanser.rainbowskill

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        Plugin = this
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object{
        lateinit var Plugin:Main
    }
}
