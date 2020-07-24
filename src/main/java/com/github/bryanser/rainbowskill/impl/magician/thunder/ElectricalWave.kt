package com.github.bryanser.rainbowskill.impl.magician.thunder

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.SpeedManager
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import com.github.bryanser.rainbowskill.passive.InducedLightning
import org.bukkit.Color
import org.bukkit.Material

//向前方丢出一个黄色粒子，飞行距离是10，击中了敌人后就落下一道雷（一道雷纯属好看）造成伤害
object ElectricalWave : Skill("电气波", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 10.0),
                ConfigEntry("Speed", 0.4)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = getConfigEntry("Distance")(cd).toDouble()
        val speed = getConfigEntry("Speed")(cd).toDouble()

        val loc = cd.caster.eyeLocation.add(0.0, -0.5, 0.0)

        val player = cd.caster

        Motion.particleLine(cd, loc, Color.YELLOW, dmg, distance, speed) { e ->
            loc.world.strikeLightningEffect(loc)

            SkillUtils.damage(cd, e, dmg)

            if (InducedLightning.activing.contains(player.uniqueId)) {
                if (InducedLightning.criting[player.uniqueId]!!.containsKey(e.uniqueId)) {
                    InducedLightning.criting[player.uniqueId]!![e.uniqueId]!!.skill1 = true

                    if (InducedLightning.criting[player.uniqueId]!![e.uniqueId]!!.skill2) {
                        val eLoc = e.location

                        eLoc.world.strikeLightningEffect(eLoc)
                        SpeedManager.newData().also {
                            it.timeLength = InducedLightning.time(player).toDouble()
                            SpeedManager.addEffect(cd, e, it)
                        }

                        InducedLightning.criting[player.uniqueId]!![e.uniqueId]!!.skill1 = false
                        InducedLightning.criting[player.uniqueId]!![e.uniqueId]!!.skill2 = false
                    }
                } else {
                    InducedLightning.criting[player.uniqueId]!![e.uniqueId] =
                            InducedLightning.SkillState(true, false)
                }
            }
        }

        return true
    }

}