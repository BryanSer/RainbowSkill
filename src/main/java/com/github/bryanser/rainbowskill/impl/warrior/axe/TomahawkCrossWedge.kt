package com.github.bryanser.rainbowskill.impl.warrior.axe

import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.ImmobilizeManager
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
//技能3：战斧横劈
//对前方长5，宽3造成伤害，蓄力1.5s
object TomahawkCrossWedge : Skill(
        "战斧横劈",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("StorageTime",1.5)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val storageTime = getConfigEntry("StorageTime")(cd).toDouble()

        ImmobilizeManager.newData().also {
            it.timeLength = storageTime
            ImmobilizeManager.addEffectSelf(cd.caster,it)
        }



        object : BukkitRunnable() {
            override fun run() {
                val enemyList = SkillUtils.rangeAttack(cd, 5.0, 3.0)
                enemyList.forEach {
                    SkillUtils.damage(cd, it, dmg)
                }
                this.cancel()
            }
        }.runTaskTimer(Main.getPlugin(), (storageTime * 20).toLong(), 1)


        return true
    }

}