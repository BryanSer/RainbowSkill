package com.github.bryanser.rainbowskill.impl.minister

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.BuffZone
import com.github.bryanser.rainbowskill.motion.SkillUtils
import com.github.bryanser.rainbowskill.passive.Rejuvenation
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.LivingEntity

//技能2：圣光沐浴
//在脚下生成一个绿色粒子的3x3范围，持续3s，在这个范围内，队友走进来就能回血，其每秒恢复值就是这个技能的自定义伤害
object HolyLightBath : Skill("圣光沐浴", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 10.0),
                ConfigEntry("Range", 2.0),
                ConfigEntry("Time", 3.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val range = (getConfigEntry("Range"))(cd).toDouble()
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val time = (getConfigEntry("Time"))(cd).toInt()
        val period = 1

        val p = cd.caster

        val dmgPeriod = dmg / 20 * period

        BuffZone.castSelfBuffZone(cd, time, period, range, Color.GREEN) { e ->
            SkillUtils.damage(cd,e,dmgPeriod)
            if (Rejuvenation.activing.contains(p.uniqueId)){
                Rejuvenation.reply(p,0.01)
            }
        }

        return true
    }

}