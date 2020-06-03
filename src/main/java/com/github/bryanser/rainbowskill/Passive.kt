package com.github.bryanser.rainbowskill

import com.github.bryanser.rainbowskill.passive.*
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.io.File

abstract class Passive(
        val name: String
) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, Main.Plugin)
    }


    val file: File = File(Skill.folder, "/passive/$name.yml")

    fun loadConfig() {
        val configs = mutableListOf<ConfigEntry>()
        val cls = this::class.java
        for (df in cls.fields) {
            if (df.type == ConfigEntry::class.java) {
                configs.add(df.get(this) as ConfigEntry)
            }
        }
        loadConfig()
        if (!file.exists()) {
            val config = YamlConfiguration()
            for (cfg in configs) {
                config.set(cfg.key, cfg.defaultValue)
            }
            config.save(file)
        } else {
            val config = YamlConfiguration.loadConfiguration(file)
            for (cfg in configs) {
                val str = config.getString(cfg.key)
                cfg.reload(str)
            }
        }
    }

    abstract fun init()

    abstract fun startPassive(p: Player)

    abstract fun stopPassive(p: Player)

    companion object {
        val passives = hashMapOf<String, Passive>()

        fun init() {
            reigster(Afterglow)
            reigster(Alacrity)
            reigster(Blast)
            reigster(Corruption)
            reigster(DestructiveWind)
            reigster(Fire)
            reigster(Furious)
            reigster(IceSoul)
            reigster(InducedLightning)
            reigster(MagicShield)
            reigster(Rejuvenation)
            reigster(Uncertain)
        }

        fun reigster(passive: Passive) {
            passives[passive.name] = passive
            passive.init()
        }
    }

}