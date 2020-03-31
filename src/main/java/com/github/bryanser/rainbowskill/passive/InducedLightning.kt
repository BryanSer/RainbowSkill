package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Passive
import com.github.bryanser.rainbowskill.event.SkillCastEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import java.util.*

/**
 * 被动技能：引雷
 * 技能1和技能2命中同一个目标后会额外劈下一道雷，没有伤害，但可以把敌人减速30%，持续2s。
 */
object InducedLightning : Passive("引雷") {
    val activing = hashSetOf<UUID>()

    val time = ConfigEntry("Time", 2.0)

    data class SkillState(
            var skill1: Boolean,
            var skill2: Boolean
    )



    val criting = hashMapOf<UUID, MutableMap<UUID,SkillState>>()

    override fun init() {
    }

    override fun startPassive(p: Player) {
        activing.add(p.uniqueId)
        criting[p.uniqueId] = mutableMapOf()
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
    }

}