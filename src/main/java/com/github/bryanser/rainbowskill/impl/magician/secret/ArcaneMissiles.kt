package com.github.bryanser.rainbowskill.impl.magician.secret

import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ArcaneMissiles : Skill("奥术飞弹", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        val sas1 = SkillUtils.getArmorStand(player,SkillUtils.getLoc(player,false), Material.FIRE,false)
        val sas2 = SkillUtils.getArmorStand(player,player.location, Material.FIRE,false)
        val sas3 = SkillUtils.getArmorStand(player,SkillUtils.getLoc(player,true), Material.FIRE,false)

        val vec = player.location.direction.normalize()
        object : BukkitRunnable() {
            var time = 0
            override fun run() {
                if (time++ >= 240) {
                    this.cancel()
                }
                sas1.velocity = vec
                SkillUtils.isDamage(sas1,player)
                sas2.velocity = vec
                SkillUtils.isDamage(sas2,player)
                sas3.velocity = vec
                SkillUtils.isDamage(sas3,player)
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}