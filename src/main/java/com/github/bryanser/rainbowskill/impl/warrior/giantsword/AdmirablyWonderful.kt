package com.github.bryanser.rainbowskill.impl.warrior.giantsword

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.Motion
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.github.bryanser.rainbowskill.impl.idleman.FallenPalm
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


//对前方长5宽1范围打出绿色粒子，造成伤害的同时还将击飞，并沉默敌人1s
object AdmirablyWonderful : Skill(
        "石破天惊",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster
        val dmg = (getConfigEntry("damage"))(cd).toDouble()
        val distance = (getConfigEntry("distance"))(cd).toDouble()

        val enemyList = SkillUtils.rangeAttack(cd, 2.0, 3.0)
        enemyList.forEach {
            SkillUtils.damage(cd,it,dmg)
            Motion.knock(cd,it,distance)
        }

        return true
    }
}