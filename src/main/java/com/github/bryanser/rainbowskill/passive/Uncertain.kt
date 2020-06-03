package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.*
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import java.util.*
import kotlin.random.Random

/**
 * 每分钟会从以下三个buff中随机给一个buff，
 * 一分钟后随机刷新一个新的buff
 * 加速2%，普攻伤害增加3%，防御力增加1%
 */
object Uncertain : Passive("变换莫测") {
    const val KEY = "变换莫测"
    data class Info(
            var time: Long = System.currentTimeMillis(),
            var type: Int = 0
    )
    val speed = ConfigEntry("Speed",0.02)
    val damage = ConfigEntry("Damage",0.03)
    val defence = ConfigEntry("Defence",0.01)

    val activing = hashMapOf<UUID, Info>()

//    val time = ConfigEntry("Time", 30.0)
    override fun init() {
        Bukkit.getScheduler().runTaskTimer(Main.Plugin, {
            val now = System.currentTimeMillis()
            val iter = activing.iterator()
            while (iter.hasNext()) {
                val (uid, info) = iter.next()
                val p = Bukkit.getPlayer(uid)
                if (p == null) {
                    iter.remove()
                    continue
                }
                val pass = now - info.time
                if(pass >= 60 * 1000L || info.type == 0){
                    info.type = 1 + Random.Default.nextInt(3)
                }
                when(info.type){
                    1-> {
                        val attr = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                        attr.remove()
                        attr.addModifier(AttributeModifier(KEY,speed(p).toDouble(),AttributeModifier.Operation.MULTIPLY_SCALAR_1))
                    }
                    2->{
                        val attr = p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
                        attr.remove()
                        attr.addModifier(AttributeModifier(KEY,damage(p).toDouble(),AttributeModifier.Operation.MULTIPLY_SCALAR_1))
                    }
                    3->{
                        val ap = AttributeManager[p]
                        ap["Defence"]?.also{
                            it.removeAttribute(KEY)
                            it.add(com.github.bryanser.rainbowskill.AttributeModifier(ValueType.MULTIPLY, defence(p).toDouble(), KEY))
                        }
                    }
                }
            }
        }, 20, 20)
    }

    fun AttributeInstance.remove(){
        for(m in this.modifiers){
            if(m.name == KEY){
                this.removeModifier(m)
                return
            }
        }
    }

    override fun startPassive(p: Player) {
        activing[p.uniqueId] = Info()
    }

    override fun stopPassive(p: Player) {
        activing.remove(p.uniqueId)
        p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).remove()
        p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).remove()
        val ap = AttributeManager[p]
        ap["Defence"]?.also {
            it.removeAttribute(KEY)
        }
    }

}