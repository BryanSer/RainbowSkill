package com.github.bryanser.rainbowskill.impl.magician.thunder

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.passive.InducedLightning
import org.bukkit.Material
import org.bukkit.entity.LivingEntity

/**
 * 在15格内选取一个2x2的区域，劈下一道雷，并造成伤害，沉默区域内敌人2s
 */
object StruckByLightning : Skill("雷劈", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Range", 1.0),
                ConfigEntry("Time", 2.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val player = cd.caster

        val time = getConfigEntry("Time")(cd).toDouble()
        val rng = getConfigEntry("Range")(cd).toInt()

        val target = player.getTargetBlock(setOf(Material.AIR), 1).location

        for (e in target.world.getNearbyEntities(
                target,
                (rng - 1.toDouble()),
                1.0,
                (rng - 1.toDouble()))) {
            if (e is LivingEntity && e != player) {

                val currLoc = e.location
                currLoc.world.strikeLightningEffect(currLoc)
                SilentManager.newData().also {
                    it.timeLength = time
                    SilentManager.addEffect(cd, e, it)
                }

                if (InducedLightning.activing.contains(player.uniqueId)) {
                    if (InducedLightning.criting[player.uniqueId]!!.containsKey(e.uniqueId)) {
                        InducedLightning.criting[player.uniqueId]!![e.uniqueId]!!.skill2 = true
                        if (InducedLightning.criting[player.uniqueId]!![e.uniqueId]!!.skill1) {
                            currLoc.world.strikeLightningEffect(currLoc)
                            SpeedManager.newData().also {
                                it.timeLength = InducedLightning.time(player).toDouble()
                                SpeedManager.addEffect(cd, e, it)
                            }
                            InducedLightning.criting[player.uniqueId]!![e.uniqueId]!!.skill1 = false
                            InducedLightning.criting[player.uniqueId]!![e.uniqueId]!!.skill2 = false
                        }
                    } else {
                        InducedLightning.criting[player.uniqueId]!![e.uniqueId] =
                                InducedLightning.SkillState(false, true)
                    }
                }

            }
        }
        return true
    }

}