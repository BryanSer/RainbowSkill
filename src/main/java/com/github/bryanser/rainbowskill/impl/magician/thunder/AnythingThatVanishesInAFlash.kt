package com.github.bryanser.rainbowskill.impl.magician.thunder

import com.github.bryanser.brapi.Utils
import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.PI

//电光石火
//对半径3以内的敌人造成伤害，并减速3s，沉默1s，并立刻劈下5道雷在身边
object AnythingThatVanishesInAFlash : Skill("电光石火", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Radius", 3.0),
                ConfigEntry("DecelerationTime", 3.0),
                ConfigEntry("SilenceTime", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster

        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val radius = getConfigEntry("Radius")(cd).toDouble()
        val decelerationTime = getConfigEntry("DecelerationTime")(cd).toDouble()
        val silenceTime = getConfigEntry("SilenceTime")(cd).toDouble()

        for (e in player.getNearbyEntities(radius, 1.0, radius)) {
            if (e == player) {
                continue
            } else if (e is LivingEntity) {
                SkillUtils.damage(cd, e, dmg)
                SpeedManager.newData().also { speedData ->
                    speedData.modifier = -0.1
                    speedData.timeLength = decelerationTime
                    SpeedManager.addEffect(cd, e, speedData)
                }
                SilentManager.newData().also { silentData ->
                    silentData.timeLength = silenceTime
                    SilentManager.addEffect(cd, e, silentData)
                }
            }
        }


        val loc = player.location
        var angle = 0.0

        for (i in 0 until 5) {
            angle += 2 * PI / 5
            val currLoc = loc.clone().add(SkillUtils.getVec(angle))
            currLoc.world.strikeLightningEffect(currLoc)
        }

        return true
    }

}