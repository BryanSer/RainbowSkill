package com.github.bryanser.rainbowskill

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        Plugin = this
        Passive.init()
        getCommand("RainbowSkillPassive").executor = CommandExecutor { sender, command, label, args ->
            if(!sender.isOp){
                return@CommandExecutor true
            }
            if(args.size >= 2 && sender is Player){
                val name = args[1]
                val passive = Passive.passives[name]
                if(passive == null){
                    sender.sendMessage("§c找不到被动 $name")
                    return@CommandExecutor true
                }
                if(args[0].equals("on", true)){
                    passive.startPassive(sender)
                    sender.sendMessage("§6被动$name 已启动")
                    return@CommandExecutor true
                }
                if(args[0].equals("off", true)){
                    passive.startPassive(sender)
                    sender.sendMessage("§6被动$name 已关闭")
                    return@CommandExecutor true
                }
            }

            return@CommandExecutor false
        }
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
