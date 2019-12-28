package com.github.bryanser.rainbowskill

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        Plugin = this
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender.isOp && sender is Player) {
            val ski = Skill.skills[args[0]]
            val lv = args.getOrElse(1) { "1" }.toInt()
            ski?.cast(sender, lv) ?: return false
            sender.sendMessage("§6已释放技能${ski.name}")
            return true
        }
        return false
    }

    companion object {
        lateinit var Plugin: Main
    }
}
