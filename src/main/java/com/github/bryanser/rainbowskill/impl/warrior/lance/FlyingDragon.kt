package com.github.bryanser.rainbowskill.impl.warrior.lance

import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.impl.warrior.axe.TomahawkPunishment
import com.github.bryanser.rainbowskill.motion.SkillUtils
import com.github.bryanser.rainbowskill.passive.removeAttribute
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable


/**
 * 在长度25格内选区，选区范围是4x4，
 * 释放后，玩家会跳入那4x4的选区，
 * 落地时会对选区内的敌人造成伤害并减速2s，沉默2s
 */
object FlyingDragon : Skill("天翔之龙", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("DecelerationTime", 2.0),
                ConfigEntry("SilenceTime", 2.0),
                ConfigEntry("Range", 4.0)
        )) {

    override fun onCast(cd: CastData): Boolean {
        val dmg = getConfigEntry("Damage")(cd).toDouble()
        val decelerationTime = getConfigEntry("DecelerationTime")(cd).toDouble()

        val silenceTime = getConfigEntry("SilenceTime")(cd).toDouble()

        val range = getConfigEntry("Range")(cd).toDouble()

        val p = cd.caster

        val target = p.getTargetBlock(mutableSetOf(Material.AIR), 50).location.direction

        p.teleport(target.toLocation(p.world))

        object : BukkitRunnable() {
            override fun run() {
                if (p.isOnGround){
                    val damaged = hashSetOf<Int>()
                    for (e in p.getNearbyEntities(range, range, range)) {
                        if (e == p) {
                            continue
                        } else if (e is LivingEntity && e.entityId !in damaged) {
                            SkillUtils.damage(cd, e, dmg)
                            damaged += e.entityId
                            //减速
                            SpeedManager.newData().also {
                                it.modifier = -0.04
                                it.timeLength = decelerationTime
                                SpeedManager.addEffect(CastData(p, 1), e, it)
                            }
                            //沉默
                            SilentManager.newData().also {
                                it.timeLength = silenceTime
                                SilentManager.addEffect(CastData(p, 1), e, it)
                            }
                        }
                    }
                    this.cancel()
                }
            }
        }.runTaskTimer(Main.getPlugin(), (5 * 20).toLong(), 1)

        return true
    }

}