package com.github.bryanser.rainbowskill.impl.magician.fire

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
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

object FireWall : Skill(
        "火墙",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Time", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val long: Double = 3.0
        val width: Double = 5.0
        val height: Double = 4.0

        val dmg = (getConfigEntry("damage"))(cd).toDouble()
        val time = (getConfigEntry("Time"))(cd).toDouble()

        Motion.wall(cd, Material.FIRE, time, dmg, long, width, height)
        return true
    }

}