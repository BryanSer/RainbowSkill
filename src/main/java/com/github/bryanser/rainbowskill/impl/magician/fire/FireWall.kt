package com.github.bryanser.rainbowskill.impl.magician.fire

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.Motion
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class FireWall : Skill("火墙", mutableListOf(""), Material.REDSTONE) {

    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {
        val long: Double = 3.0
        val width: Double = 5.0
        val height: Double = 4.0

        Motion.wall(player, Material.FIRE, 2.5, long, width, height)
        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}