package com.github.bryanser.rainbowskill

import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

abstract class EffectManager<ED : EffectManager.EffectData<*>>(
        val period: Long
) : BukkitRunnable(), Listener {

    interface EffectData<T> {
        var isBenefit: Boolean
    }

    open fun init() {
        this.runTaskTimer(Main.Plugin, period, period)
        Bukkit.getPluginManager().registerEvents(this, Main.Plugin)
    }


    abstract fun addEffect(target: LivingEntity, data: ED): Boolean

    abstract fun newData(): ED


}

object SpeedManager : EffectManager<SpeedManager.SpeedData>(2) {
    internal val data = hashMapOf<UUID, Info>()

    internal data class Info(
            val uuid: UUID,
            val effect: MutableList<SpeedData> = mutableListOf()
    ) {
        fun isAlive(): Boolean = Bukkit.getEntity(uuid)?.isValid ?: false
    }

    data class SpeedData(
            var modifier: Double,
            /**
             * 秒
             */
            var timeLength: Double
    ) : EffectData<SpeedData> {
        override var isBenefit: Boolean = modifier > 0
    }

    override fun addEffect(target: LivingEntity, data: SpeedData): Boolean {
        return true
    }

    override fun newData(): SpeedData = SpeedData(0.0, 0.0)
    override fun run() {

    }

    override fun init() {
        super.init()
    }
}

//属性加成
//眩晕
//着火
//移动速度 减速
//无敌
//定身
//沉默
//失明
//冰冻
