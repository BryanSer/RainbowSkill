package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Passive
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.*

/**
 * 被动技能：心眼
 * 在此英雄半径7以内的敌方英雄会有绿色粒子出现在头上，只有此英雄可以看见这个粒子。
 */
object Mind : Passive("心眼") {
    const val KEY = "心眼"
    val activing = hashSetOf<UUID>()

    val range = ConfigEntry("range", 3.0)
    val time = ConfigEntry("time", 2.0)

    override fun init() {
        Bukkit.getScheduler().runTaskTimer(Main.Plugin, {
            for (uid in activing) {
                val p = Bukkit.getPlayer(uid) ?: continue
                val range = range(p).toDouble()
                for (e in p.getNearbyEntities(range, 1.0, range)) {
                    if (e == p) {
                        continue
                    } else if (e is LivingEntity && e is Player) {
                        val loc = e.location.add(0.0,1.0,0.0)
                        val playerList = mutableListOf<Player>()
                        playerList.add(e)
                        ParticleEffect.REDSTONE.display(ParticleEffect.OrdinaryColor(Color.GREEN), loc, playerList)
                    }
                }
            }
        }, 5, 5)
    }

    override fun startPassive(p: Player) {
        activing.add(p.uniqueId)
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }

}