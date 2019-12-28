package com.github.bryanser.rainbowskill.impl.warrior.giantsword

import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

//使用技能后身边出现红色粒子，持续3s，被击中的敌人都会被击飞，提升自己的15%暴击几率
class TigerHeart : Skill("虎贲", mutableListOf(""), Material.REDSTONE) {
    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {

        val asList = mutableListOf<ArmorStand>()
        for (i in 0 until 8){
            val ins = SkillUtils.getArmorStand(player, player.location, Material.IRON_AXE, false)
            asList.add(ins)
        }

        object : BukkitRunnable() {
            var time = 0
            var angle: Double = 0.0
            var pi = Math.PI

            fun getLocation(loc: Location, angle: Double): Location {
                return loc.clone().add(2 * cos(angle), 0.0, 2 * sin(angle))
            }

            override fun run() {
                val loc = player.location

                if (time++ >= 60) {
                    this.cancel()
                }
                asList.forEach{
                    angle += 2 * pi / 8
                    it.teleport(getLocation(loc, angle))
                }
            }
        }.runTaskTimer(Main.Plugin, 1, 1)


        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}