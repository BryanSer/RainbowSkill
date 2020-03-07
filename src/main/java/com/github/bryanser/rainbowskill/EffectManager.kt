package com.github.bryanser.rainbowskill

import com.github.bryanser.rainbowskill.event.SkillCastEvent
import com.github.bryanser.rainbowskill.motion.isFriendly
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

abstract class EffectManager<ED : EffectManager.EffectData<*>>(
        val period: Long,
        val friendly: Boolean = false
) : BukkitRunnable(), Listener {

    interface EffectData<T> {
        var isBenefit: Boolean
    }

    open fun init() {
        if (period > 0) {
            this.runTaskTimer(Main.Plugin, period, period)
        }
        Bukkit.getPluginManager().registerEvents(this, Main.Plugin)
    }

    open fun addEffect(cd: CastData, target: LivingEntity, data: ED): Boolean {
        if (!friendly) {
            val caster = cd.caster
            if (caster.isFriendly(target)) {
                return false
            }
        }
        return addEffect(target, data)
    }

    protected abstract fun addEffect(target: LivingEntity, data: ED): Boolean

    abstract fun newData(): ED


}

//速度
object SpeedManager : EffectManager<SpeedManager.SpeedData>(3) {
    const val MODIFIER_NAME = "rainbow_speedmanager_effect"
    internal val data = hashMapOf<UUID, Info>()

    internal data class Info(
            val uuid: UUID,
            val effect: MutableList<SpeedData> = LinkedList()
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
        var startTime: Long = 0L

        val endTime: Long by lazy {
            startTime + (timeLength * 1000L).toLong()
        }

        fun isValid(): Boolean {
            return System.currentTimeMillis() < endTime
        }

        override var isBenefit: Boolean = modifier > 0
    }

    override fun addEffect(target: LivingEntity, data: SpeedData): Boolean {
        this.data.getOrPut(target.uniqueId) { Info(target.uniqueId) }.effect.add(
                data.also {
                    it.startTime = System.currentTimeMillis()
                }
        )
        return true
    }

    override fun newData(): SpeedData = SpeedData(0.0, 0.0)
    override fun run() {
        val it = data.iterator()
        while (it.hasNext()) {
            val info = it.next().value
            if (!info.isAlive()) {
                it.remove()
                continue
            }
            val target = Bukkit.getEntity(info.uuid) as? LivingEntity
            if (target == null) {
                it.remove()
                continue
            }
            val iter = info.effect.iterator()
            val buff = mutableListOf<Double>()
            val debuff = mutableListOf<Double>()
            while (iter.hasNext()) {
                val eff = iter.next()
                if (!eff.isValid()) {
                    iter.remove()
                    continue
                }
                (if (eff.isBenefit) buff else debuff).add(eff.modifier)
            }
            var effect = 0.0
            if (buff.isNotEmpty()) {
                buff.sortDescending()
                var i = 1
                for (e in buff) {
                    effect += e / i++
                }
            }
            if (debuff.isNotEmpty()) {
                debuff.sort()
                var i = 1
                for (e in buff) {
                    effect += e / i++
                }
            }
            if (effect != 0.0) {
                val attr = target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                attr.modifiers.iterator().run {
                    while (hasNext()) {
                        val mod = next()
                        if (mod.name == MODIFIER_NAME) {
                            remove()
                            continue
                        }
                    }
                }
                attr.addModifier(AttributeModifier(MODIFIER_NAME, effect, AttributeModifier.Operation.ADD_SCALAR))
            }
        }
    }
}

//眩晕
object VertigoManager : EffectManager<VertigoManager.VertigoData>(-1) {

    val endTime = hashMapOf<UUID, Long>()

    data class VertigoData(
            var timeLength: Double
    ) : EffectData<VertigoData> {
        override var isBenefit: Boolean = false
    }

    @EventHandler
    fun onMove(evt: PlayerMoveEvent) {
        val end = endTime[evt.player.uniqueId] ?: return
        if (System.currentTimeMillis() < end) {
            evt.isCancelled = true
        }
    }

    @EventHandler
    fun onCast(evt: SkillCastEvent) {
        val end = endTime[evt.player.uniqueId] ?: return
        if (System.currentTimeMillis() < end) {
            evt.isCancelled = true
        }
    }

    override fun addEffect(target: LivingEntity, data: VertigoData): Boolean {
        val last = endTime[target.uniqueId] ?: 0L
        val end = System.currentTimeMillis() + (data.timeLength * 1000L).toLong()
        if (end > last) {
            endTime[target.uniqueId] = end
        }
        if (target !is Player) {
            target.setAI(false)
            TODO()
        }
        return true
    }

    override fun newData(): VertigoData = VertigoData(0.0)

    override fun run() {
    }

}

//失明
object BlindnessManager : EffectManager<BlindnessManager.GoBlindData>(2) {

    data class GoBlindData(
            /**
             * 秒
             */
            var timeLength: Double) : EffectData<GoBlindData> {
        override var isBenefit: Boolean = false
    }

    override fun addEffect(target: LivingEntity, data: GoBlindData): Boolean {
        return true
    }

    override fun newData(): GoBlindData = GoBlindData(0.0)

    override fun run() {
    }


}

//无敌
object InvincibleManager : EffectManager<InvincibleManager.Invincibledata>(2) {

    data class Invincibledata(
            /**
             * 秒
             */
            var timeLength: Double) : EffectData<Invincibledata> {
        override var isBenefit: Boolean = true
    }

    override fun addEffect(target: LivingEntity, data: Invincibledata): Boolean {
        return true
    }

    override fun newData(): Invincibledata = Invincibledata(0.0)

    override fun run() {
    }
}

//定身
object ImmobilizeManager : EffectManager<ImmobilizeManager.ImmobilizeData>(2) {
    data class ImmobilizeData(
            /**
             * 秒
             */
            var timeLength: Double) : EffectData<ImmobilizeData> {
        override var isBenefit: Boolean = false
    }

    override fun addEffect(target: LivingEntity, data: ImmobilizeData): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun addEffectSelf(target: LivingEntity, data: ImmobilizeData): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun newData(): ImmobilizeData = ImmobilizeData(0.0)

    override fun run() {
    }
}

//沉默
object SilentManager : EffectManager<SilentManager.SilentData>(2) {
    data class SilentData(
            /**
             * 秒
             */
            var timeLength: Double) : EffectData<SilentData> {
        override var isBenefit: Boolean = false
    }

    override fun addEffect(target: LivingEntity, data: SilentData): Boolean {
        return true;
    }

    override fun newData(): SilentData = SilentData(0.0)

    override fun run() {
    }
}

//着火
object InflameManager : EffectManager<InflameManager.InflameData>(2) {
    data class InflameData(
            /**
             * 秒
             */
            var timeLength: Double) : EffectData<InflameData> {
        override var isBenefit: Boolean = false
    }

    override fun addEffect(target: LivingEntity, data: InflameData): Boolean {
        return true
    }

    override fun newData(): InflameData = InflameData(0.0)

    override fun run() {
    }
}

//冰冻
object FrozenManager : EffectManager<FrozenManager.FrozenData>(2) {
    data class FrozenData(
            /**
             * 秒
             */
            var timeLength: Double) : EffectData<FrozenData> {
        override var isBenefit: Boolean = false
    }

    override fun addEffect(target: LivingEntity, data: FrozenData): Boolean {
        return true
    }

    override fun newData(): FrozenData = FrozenData(0.0)

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
