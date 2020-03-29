package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Passive
import org.bukkit.entity.Player
import java.util.*

/**
 * 每分钟会从以下三个buff中随机给一个buff，
 * 一分钟后随机刷新一个新的buff
 * 加速2%，普攻伤害增加3%，防御力增加1%
 */
object Uncertain : Passive("变换莫测") {
    val activing = hashMapOf<UUID, Long>()

    val time = ConfigEntry("Time", 30.0)
    override fun init() {
    }

    override fun startPassive(p: Player) {
        activing[p.uniqueId] = 0L
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }

}