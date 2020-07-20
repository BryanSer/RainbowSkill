package com.github.bryanser.rainbowskill.impl.shaman

import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.impl.magician.fire.PillarOfFlame
import com.github.bryanser.rainbowskill.motion.SkillUtils
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

/**
 * 技能4：陨星术
 * 在25格内选区一个4x4的范围，然后会落下一堆黑色红色粒子在那个区域，区域内的敌人会掉血，并减速3s，失明3s
 */
object Meteorology : Skill(
        "陨星术",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("DecelerationTime",3.0),
                ConfigEntry("BlindnessTime",3.0),
                ConfigEntry("Range", 4.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = getConfigEntry("Damage")(cd).toDouble()
        val range = getConfigEntry("Range")(cd).toDouble()

        val decelerationTime =getConfigEntry("DecelerationTime")(cd).toDouble()
        val blindnessTime =getConfigEntry("BlindnessTime")(cd).toDouble()

        val p = cd.caster

        val target = p.getTargetBlock(mutableSetOf(Material.AIR), 50).location

        object : BukkitRunnable() {
            val damaged = mutableSetOf<Int>()
            override fun run() {
                var index = 0
                for (i in -(range/2).toInt() until (range/2).toInt()){
                    for (j in -(range/2).toInt() until (range/2).toInt()){
                        val currLoc = target.clone().add(i.toDouble(), 0.0, j.toDouble())
                        if (index%2==0){
                            ParticleEffect.REDSTONE.display(ParticleEffect.OrdinaryColor(Color.RED), currLoc, 50.0)
                        }else{
                            ParticleEffect.REDSTONE.display(ParticleEffect.OrdinaryColor(Color.BLACK), currLoc, 50.0)
                        }
                        index++
                    }
                }

                for (e in target.world.getNearbyEntities(target,range,1.0,range)){
                    if (e is LivingEntity && !damaged.contains(e.entityId)){
                        damaged.add(e.entityId)
                        SkillUtils.damage(cd,e,dmg)
                        SpeedManager.newData().also {
                            it.modifier = -0.5
                            it.timeLength = decelerationTime
                            SpeedManager.addEffect(CastData(p, 1), e, it)
                        }
                        //失明
                        BlindnessManager.newData().also {
                            it.timeLength = blindnessTime
                            BlindnessManager.addEffect(cd, e, it)
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(), (2 * 20).toLong(), 1)

        return true
    }
}