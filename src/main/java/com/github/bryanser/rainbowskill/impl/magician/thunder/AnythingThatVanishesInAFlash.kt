package com.github.bryanser.rainbowskill.impl.magician.thunder

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

//对半径3以内的敌人造成伤害，并减速3s，沉默1s，并立刻劈下5道雷在身边
object AnythingThatVanishesInAFlash : Skill("电光石火", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Radius", 3.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster

        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val radius = getConfigEntry("Radius")(cd).toDouble()
        for (e in player.getNearbyEntities(3.0, 1.0, 3.0)) {
            if (e == player) {
                continue
            } else if (e is LivingEntity) {
                SkillUtils.damage(cd, e, dmg)
                SpeedManager.newData().also { speedData ->
                    speedData.modifier = -0.1
                    speedData.timeLength = 3.0
                    SpeedManager.addEffect(e, speedData)
                }
                SilentManager.newData().also { silentData ->
                    silentData.modifier = -0.1
                    silentData.timeLength = 1.0
                    SilentManager.addEffect(e, silentData)
                }
            }
        }
        object : BukkitRunnable() {
            val loc = player.location
            var t = 0
            override fun run() {
                if (t++ >= 5) {
                    this.cancel()
                }
                loc.world.strikeLightningEffect(loc)
            }
        }.runTaskTimer(Main.Plugin, 1, 1)


        return true
    }

}