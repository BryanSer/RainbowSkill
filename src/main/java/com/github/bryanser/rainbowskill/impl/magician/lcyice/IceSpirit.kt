package com.github.bryanser.rainbowskill.impl.magician.lcyice

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

//把前方5格地上的方块变成冰块，持续3s后恢复原本的方块，敌人碰着了冰块就掉血，并定身1s
object IceSpirit : Skill(
        "冰魄",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 0.0),
                ConfigEntry("Long", 5.0),
                ConfigEntry("Time", 3.0),
                ConfigEntry("SettlingTime", 1.0)
        )) {

    override fun onCast(cd: CastData): Boolean {
        val dmg = (getConfigEntry("Damage"))(cd).toDouble()
        val time = getConfigEntry("Time")(cd).toDouble()
        val l = getConfigEntry("Long")(cd).toInt()
        val settlingTime = getConfigEntry("SettlingTime")(cd).toDouble()

        val player = cd.caster

        val location = player.location

        val vec = location.direction.normalize()
        vec.y = 0.0

        val loc = player.location.add(0.0, -1.0, 0.0)

        val type = mutableMapOf<Location, Material>()

        if (!player.isOnGround){
            return true
        }

        for (i in 0 until l) {
            val currLoc = loc.add(vec)
            type[currLoc.clone()] = currLoc.block.type
            currLoc.block.type = Material.ICE
            currLoc.block.state.update()
        }

        object : BukkitRunnable() {
            var t = 0

            val damaged = hashSetOf<Int>()

            override fun run() {
                if (t++ >= time * 20) {
                    type.forEach { (t, u) ->
                        t.block.type = u
                    }
                    this.cancel()
                    return
                }
                type.forEach { t ->
                    for (e in t.key.block.world.getNearbyEntities(t.key, 0.25, 0.25, 0.25)) {
                        if (e == player) {
                            continue
                        } else if (e is LivingEntity && e.entityId !in damaged) {
                            SkillUtils.damage(cd, e, dmg)
                            damaged += e.entityId
                            //定身
                            ImmobilizeManager.newData().also {
                                it.modifier= 0.0
                                it.timeLength = settlingTime
                                ImmobilizeManager.addEffect(e,it)
                            }
                        }
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, 1, 1)

        return true
    }
}