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

//速度
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

//眩晕
object VertigoManager : EffectManager<VertigoManager.VertigoData>(2) {

    data class VertigoData(var modifier: Double,
                           /**
                            * 秒
                            */
                           var timeLength: Double
    ) : EffectData<VertigoData> {
        override var isBenefit: Boolean = modifier > 0
    }

    override fun addEffect(target: LivingEntity, data: VertigoData): Boolean {
        return true
    }

    override fun newData(): VertigoData = VertigoData(0.0, 0.0)

    override fun run() {

    }

}

//失明
object GoBlindManager : EffectManager<GoBlindManager.GoBlindData>(2) {

    data class GoBlindData(var modifier: Double,
                           /**
                            * 秒
                            */
                           var timeLength: Double) : EffectData<GoBlindData> {
        override var isBenefit: Boolean = modifier > 0
    }

    override fun addEffect(target: LivingEntity, data: GoBlindData): Boolean {
        return true
    }

    override fun newData(): GoBlindData = GoBlindData(0.0, 0.0)

    override fun run() {
    }


}

//无敌
object InvincibleManager : EffectManager<InvincibleManager.Invincibledata>(2) {

    data class Invincibledata(var modifier: Double,
                              /**
                               * 秒
                               */
                              var timeLength: Double) : EffectData<Invincibledata> {
        override var isBenefit: Boolean = modifier > 0
    }

    override fun addEffect(target: LivingEntity, data: Invincibledata): Boolean {
        return true
    }

    override fun newData(): Invincibledata = Invincibledata(0.0, 0.0)

    override fun run() {
    }
}

//定身
object ImmobilizeManager: EffectManager<ImmobilizeManager.ImmobilizeData>(2){
    data class ImmobilizeData(var modifier: Double,
                              /**
                               * 秒
                               */
                              var timeLength: Double) : EffectData<ImmobilizeData> {
        override var isBenefit: Boolean = modifier > 0
    }

    override fun addEffect(target: LivingEntity, data: ImmobilizeData): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun newData(): ImmobilizeData = ImmobilizeData(0.0,0.0)

    override fun run() {
    }
}

//沉默
object SilentManager: EffectManager<SilentManager.SilentData>(2){
    data class SilentData(var modifier: Double,
                              /**
                               * 秒
                               */
                              var timeLength: Double) : EffectData<SilentData> {
        override var isBenefit: Boolean = modifier > 0
    }

    override fun addEffect(target: LivingEntity, data: SilentData): Boolean {
        return true;
    }

    override fun newData(): SilentData = SilentData(0.0,0.0)

    override fun run() {
    }
}

//着火
object InflameManager: EffectManager<InflameManager.InflameData>(2){
    data class InflameData(var modifier: Double,
                           /**
                            * 秒
                            */
                           var timeLength: Double) : EffectData<InflameData> {
        override var isBenefit: Boolean = modifier > 0
    }

    override fun addEffect(target: LivingEntity, data: InflameData): Boolean {
        return true
    }

    override fun newData(): InflameData = InflameData(0.0,0.0)

    override fun run() {
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
