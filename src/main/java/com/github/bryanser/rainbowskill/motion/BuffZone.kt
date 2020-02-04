package com.github.bryanser.rainbowskill.motion

import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin

object BuffZone {

    /**
     * @param period 单位tick
     */
    fun castSelfBuffZone(ci: CastData, time: Int, period: Int, range: Double, color: Color, effect: (LivingEntity) -> Unit) {
        val p = ci.caster
        object : BukkitRunnable() {
            var count = 0
            override fun run() {
                if (ci.cancel) {
                    this.cancel()
                    return
                }
                if (count / 20 >= time) {
                    this.cancel()
                    return
                }

                if (count % 3 == 0) {
                    val loc = p.location
                    Bukkit.getScheduler().runTaskAsynchronously(Main.Plugin) {
                        var st = 0.0
                        while (st < Math.PI * 2) {
                            val t = loc.clone().add(Math.cos(st) * range, 0.0, Math.sin(st) * range)
                            ParticleEffect.REDSTONE.display(ParticleEffect.OrdinaryColor(color), loc, 50.0)
                            st += Math.PI / 12
                        }
                    }
                }
                if (count++ % period == 0) {
                    val loc = p.location
                    for (e in loc.world.getNearbyEntities(loc, range, range, range)) {
                        if (e is LivingEntity) {
                            effect(e)
                        }
                    }

                }
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
    }

    fun castBuffZone(ci: CastData, ttime: Int, tt: Int, r: Double, rng: Int) {
        val pp = ci.caster
        //val rng = length(pp).toInt()
        val target = pp.getTargetBlock(setOf(Material.AIR), rng)?.location ?: return

//        val ttime = this.time(pp).toInt()
//        val r = radius(pp).toDouble()
//        val tt = triggerTick(pp).toInt()
//        val once = this.once(pp).toBoolean()
        val p = 8
        val task = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.Plugin, {
            var st = 0.0
            val add = Math.PI / p
            while (st <= Math.PI * 2) {
                val x = cos(st) * r
                val z = sin(st) * r
//                for ((par, y) in particle) {
//                    val loc = target.clone().add(x, y, z)
//                    par.play(loc)
//                }
                st += add
            }
        }, 5, 5)
        object : BukkitRunnable() {
            val casted = mutableSetOf<Int>()
            var time = -1
            override fun run() {
                if (time++ >= ttime) {
                    this.cancel()
                    task.cancel()
                    return
                }
                if (time % tt == 0) {
                    for (t in target.world.getNearbyEntities(target, r, r, r)) {
                        if (casted.contains(t.entityId) || t !is LivingEntity) {
                            continue
                        }
                        casted += t.entityId
//                        for (m in motion) {
//                            PassiveManager.attackEntity[ci.castId] = t
//                            m.cast(CastInfo(pp, t, ci.castId))
//                        }
                    }
                }
            }

        }.runTaskTimer(Main.Plugin, 1, 1)
    }
}