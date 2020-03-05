package com.github.bryanser.rainbowskill.impl.shooter.knighterrant

import com.github.bryanser.rainbowskill.*
import com.github.bryanser.rainbowskill.impl.shooter.ArrowHitEffect
import com.github.bryanser.rainbowskill.impl.shooter.ArrowPenetrate
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.scheduler.BukkitScheduler

//技能1：爆炸射击s
//射出一根能飞行40格的箭，后面跟着红色粒子，
// 碰撞了方块或者实体后发生小爆炸，炸开敌人，不造成伤害
object ExplosiveFire : Skill("爆炸射击", mutableListOf(""), Material.REDSTONE,
        listOf(
                ConfigEntry(COOLDOWN_KEY, 10.0),
                ConfigEntry("Distance", 40.0)
        )) {
    override fun onCast(cd: CastData): Boolean {
        val distance = getConfigEntry("Distance")(cd).toDouble()
        val loc = cd.caster.location
        val vec = loc.direction.normalize().multiply(2)

//        ArrowPenetrate.cast(cd, Material.ARROW, loc, vec, distance, true,{
//            val curr = it.location
//            ParticleEffect.REDSTONE.display(ParticleEffect.OrdinaryColor(Color.RED), curr.clone(), 50.0)
//
//        }) {
//            it.world.createExplosion(it.location,0.0F)
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
            loc.world.createExplosion(loc.x, loc.y, loc.z, 1F, false, false)
            t?.remove()
            task.cancel()
        }

        return true
    }

}