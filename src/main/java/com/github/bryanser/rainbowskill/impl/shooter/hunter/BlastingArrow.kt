package com.github.bryanser.rainbowskill.impl.shooter.hunter

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.ConfigEntry
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.shooter.ArrowHitEffect
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.scheduler.BukkitRunnable

//射出一支飞行20格的箭，碰撞了实体或方块后发生小爆炸，
// 接下来的五秒内再发生三次爆炸，每次都能弹开敌人并造成伤害（自定义）
object BlastingArrow : Skill(
        "爆破箭",
        mutableListOf(""),
        Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Damage", 1.0),
                ConfigEntry("Distance", 20.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val distance = getConfigEntry("Distance")(cd).toDouble()
        val dmg = getConfigEntry("Damage")(cd).toDouble()

        val loc = cd.caster.location
        val vec = loc.direction.normalize()

        val time = 5.0

//        ArrowPenetrate.cast(cd, Material.ARROW, loc, vec, distance, false) {
//            SkillUtils.damage(cd, it, dmg)
//            val enemyLoc = it.location
//            it.world.createExplosion(enemyLoc, 0.0F)
//            object : BukkitRunnable() {
//                var t = 0
//                override fun run() {
//                    if (t++ >= time * 20) {
//                        loc.world.createExplosion(loc, dmg.toFloat())
//                        this.cancel()
//                        return
//                    }
//                    if (t == 20) {
//                        loc.world.createExplosion(loc, dmg.toFloat())
//                    }
//                    if (t == 60) {
//                        loc.world.createExplosion(loc, dmg.toFloat())
//                    }
//                }
//            }.runTaskTimer(Main.Plugin, 1, 1)
//        }

        var t: Arrow? = null
        val task = Bukkit.getScheduler().runTaskTimer(Main.Plugin, {
            t?.velocity = vec
            ParticleEffect.REDSTONE.display(ParticleEffect.OrdinaryColor(Color.RED),
                    t?.location ?: return@runTaskTimer,
                    50.0)
        }, 1, 1)

        t = ArrowHitEffect.cast(cd, Arrow::class.java, loc, distance, vec) { b, e ->
            val loc = b ?: e?.location ?: return@cast

            val tt = loc
            loc.world.createExplosion(tt.x, tt.y, tt.z, 0.0F, false, false)
            t?.remove()
            task.cancel()

            val enemyLoc = loc
            tt.world.createExplosion(tt.x, tt.y, tt.z, 0.0F, false, false)

            object : BukkitRunnable() {
                var tick = 0
                override fun run() {
                    if (tick++ >= time * 20) {
                        loc.world.createExplosion(loc.x, loc.y, loc.z, dmg.toFloat(), false, false)
                        this.cancel()
                        return
                    }
                    if (tick == 40) {
                        loc.world.createExplosion(loc.x, loc.y, loc.z, dmg.toFloat(), false, false)
                    }
                    if (tick == 70) {
                        loc.world.createExplosion(loc.x, loc.y, loc.z, dmg.toFloat(), false, false)
                    }
                }
            }.runTaskTimer(Main.Plugin, 1, 1)

        }
        return true
    }

}