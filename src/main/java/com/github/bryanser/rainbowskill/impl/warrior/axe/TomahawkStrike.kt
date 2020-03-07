package com.github.bryanser.rainbowskill.impl.warrior.axe

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

//丢出一把铁斧，击中就眩晕敌人1s，长度是12
object TomahawkStrike : Skill("战斧打击", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 12.0),
                ConfigEntry("EffectTime", 1.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = getConfigEntry("Distance")(cd).toDouble()
        val effectTime = getConfigEntry("EffectTime")(cd).toDouble()
        val player = cd.caster

        val ins = SkillUtils.getArmorStand(player, player.location, Material.IRON_AXE, false)

        object : BukkitRunnable() {
            val loc = player.location
            val vec = loc.direction.normalize()

            val dd = distance * distance

            override fun run() {
                val dis = loc.distanceSquared(ins.location)
                if (dis >= dd) {
                    ins.remove()
                    this.cancel()
                    return
                }
                ins.velocity = vec
                for (e in ins.getNearbyEntities(0.25, 1.0, 0.25)) {
                    if (e == player) {
                        continue
                    } else if (e is LivingEntity) {
                        SkillUtils.damage(cd, e, dmg)
                        VertigoManager.newData().also {
                            it.timeLength = effectTime
                            VertigoManager.addEffect(cd, e, it)
                        }
                        ins.remove()
                        this.cancel()
                        break
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
        return true
    }

}