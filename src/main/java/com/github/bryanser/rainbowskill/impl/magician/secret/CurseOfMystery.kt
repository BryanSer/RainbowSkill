package com.github.bryanser.rainbowskill.impl.magician.secret

import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.motion.Motion
import com.github.bryanser.rainbowskill.passive.Uncertain.remove
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.scheduler.BukkitRunnable

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
                ConfigEntry("Damage", 0.0),
                ConfigEntry("Distance", 15.0),
                ConfigEntry("Speed", 0.4),
                ConfigEntry("StorageTime", 3.0)
        )) {
    override fun onCast(cd: CastData): Boolean {


        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val distance = (getConfigEntry("Distance"))(cd).toDouble()
        val speed = (getConfigEntry("Speed"))(cd).toDouble()
        val storageTime = getConfigEntry("StorageTime")(cd).toDouble()

        ImmobilizeManager.newData().also {
            it.timeLength = storageTime
            ImmobilizeManager.addEffectSelf(cd.caster, it)
        }

        object : BukkitRunnable() {
            override fun run() {
                val loc = cd.caster.eyeLocation.add(0.0, -0.5, 0.0)

                Motion.particleLinePro(cd, storageTime, loc, Color.YELLOW, Color.BLUE, dmg, distance, speed) {
                    ImmobilizeManager.newData().also { data ->
                        data.timeLength = 3.0
                        ImmobilizeManager.addEffect(cd, it, data)
                    }
                    SilentManager.newData().also { data ->
                        data.timeLength = 3.0
                        SilentManager.addEffect(cd, it, data)
                    }
                    BlindnessManager.newData().also { data ->
                        data.timeLength = 3.0
                        BlindnessManager.addEffect(cd, it, data)
                    }
                }
            }
        }.runTaskLater(Main.getPlugin(), (storageTime * 20).toLong())

        return true
    }

}