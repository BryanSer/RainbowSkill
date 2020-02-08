package com.github.bryanser.rainbowskill.impl.minister

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.attribute.Attribute


//技能3：圣光闪现
//往前闪现4格，并恢复5%的生命值
object FlashOfLight : Skill("圣光闪现", mutableListOf(""), Material.REDSTONE,
listOf(
ConfigEntry(COOLDOWN_KEY, 10.0),
ConfigEntry("Distance", 15.0)
)) {
    override fun onCast(cd: CastData): Boolean {
        val distance = (getConfigEntry("Distance"))(cd).toDouble()

        Motion.flash(cd.caster, 1, distance)

        val dmg = cd.caster.getAttribute(Attribute.GENERIC_MAX_HEALTH).value * 0.05

        SkillUtils.damage(cd,cd.caster,dmg)

        return true
    }

}