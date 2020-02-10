package com.github.bryanser.rainbowskill.impl.magician.secret

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Skill
import org.bukkit.Material

//技能4：奥秘诅咒
//向前方发射一个蓝色粒子轨迹的的紫色粒子子弹，
// 飞行长度是15，
// 需要吟唱3s，
// 击中后就定身3s，沉默3s，失明3s
object CurseOfMystery : Skill(
        "奥秘诅咒",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 0.0)
        )) {
    override fun onCast(cd: CastData): Boolean {

        return true
    }

}