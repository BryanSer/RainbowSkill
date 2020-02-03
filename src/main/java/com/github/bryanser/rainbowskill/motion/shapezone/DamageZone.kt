package com.github.bryanser.artificepro.motion.impl.shapezone


import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.CastData
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

class DamageZone {
    lateinit var shape: Shape
    lateinit var key: String
//    lateinit var delay: Expression
//    lateinit var self: Expression
//    lateinit var follow: Expression

    fun cast(ci: CastData, cd: Double, delay: Int) {
//        val delay = this.delay(ci.caster).toInt()
//        val self = this.self(ci.caster).toBoolean()
//        val follow = this.follow(ci.caster).toBoolean()
        object : BukkitRunnable() {
            var tick = 0
            var loc = ci.caster.location

            init {
                shape.playEffect(loc)
            }

            override fun run() {
//                if (follow) {
//                    loc = ci.caster.location
//                }
                if (tick++ == delay) {
                    if (cd != null) {
                        for (e in shape.getDamageZoneEntities(loc)) {
                            if (e == ci.caster) {
                                continue
                            }
                            if (e !is LivingEntity) {
                                continue
                            }
//                            for (t in cd.triggers) {
//                                if (t is ShapeTrigger && t.key == key) {
//                                    t.onTrigger(e, ci.caster, ci.castId)
//                                }
//                            }
                        }
                    }
                    this.cancel()
                    return
                }
                if (tick % 3 == 0) {
                    shape.playEffect(loc)
                }
            }
        }.runTaskTimer(Main.getPlugin(), 1, 1)
    }

//    override fun loadConfig(config: ConfigurationSection) {
//        shape = Shape(config.getConfigurationSection("Shape"))
//        key = config.getString("key")
//        delay = ExpressionHelper.compileExpression(config.getString("delay"))
//        self = ExpressionHelper.compileExpression(config.getString("self"), true)
//        follow = ExpressionHelper.compileExpression(config.getString("follow"), true)
//    }
}