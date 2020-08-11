package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Passive
import com.github.bryanser.rainbowskill.SpeedManager
import com.github.bryanser.rainbowskill.motion.Motion
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*

/**
 * 在此英雄死后会在死亡的那个方块3x3x3的区域
 * 出现紫色粒子，并对区域内的英雄持续
 * 造成减速（20%），区域在2秒后消失
 */
object Corruption : Passive("腐化") {
    const val KEY = "腐化"
    val activing = hashSetOf<UUID>()

    val range = ConfigEntry("range", 3.0)
    val time = ConfigEntry("time", 2.0)

    override fun init() {
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onDead(evt: PlayerDeathEvent) {
        val p = evt.entity ?: return

        if (!activing.contains(p.uniqueId)) {
            return
        }
        Motion.particleCircle(2, p, range(p).toDouble(), 18.0, Color.PURPLE) { e ->

            SpeedManager.newData().also {
                it.modifier = -0.04
                it.timeLength = 1.0
                SpeedManager.addEffect(CastData(p, 1), e, it)
            }
        }

    }

    override fun startPassive(p: Player) {
        activing.add(p.uniqueId)
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }

}