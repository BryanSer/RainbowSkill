package com.github.bryanser.rainbowskill.motion.shapezone


import com.github.bryanser.brapi.Main
import com.github.bryanser.rainbowskill.CastData
import com.github.bryanser.rainbowskill.motion.SkillUtils
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

class DamageZone {
    lateinit var shape: Shape

    /**
     * @param follow 是否跟随发动者
     * @param delay 延迟发动
     */
    fun castDamageZone(config: ConfigurationSection, ci: CastData, dmg: Double, delay: Int, follow: Boolean) {
        shape = Shape(config.getConfigurationSection("Shape"))

        object : BukkitRunnable() {
            var tick = 0
            var loc = ci.caster.location

            init {
                shape.playEffect(loc)
            }

            override fun run() {
                if (follow) {
                    loc = ci.caster.location
                }
                if (tick++ == delay) {
                    for (e in shape.getDamageZoneEntities(loc)) {
                        if (e == ci.caster) {
                            continue
                        }
                        if (e is LivingEntity) {
                            SkillUtils.damage(ci, e, dmg)
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

}