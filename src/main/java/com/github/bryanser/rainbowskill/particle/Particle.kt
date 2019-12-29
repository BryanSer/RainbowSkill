package com.github.bryanser.rainbowskill.particle


import com.github.bryanser.rainbowskill.Main
import com.github.bryanser.rainbowskill.tools.ParticleEffect
import org.bukkit.Bukkit
import org.bukkit.Location

abstract class Particle(
        val name: String
) {
    abstract fun init(args: Array<String>)
    protected abstract fun display(loc: Location)
    open fun getParticleEffect(): ParticleEffect? = null

    fun play(loc: Location) {
        val loc = loc.clone()
        Bukkit.getScheduler().runTaskAsynchronously(Main.Plugin) {
            this.display(loc)
        }
    }
}

class SweepAttack : Particle("SweepAttack") {
    var amount = 1
    var offsetx = 0f
    var offsety = 0f
    var offsetz = 0f
    var speed = 0.25f
    override fun init(args: Array<String>) {
        amount = args.getOrNull(0)?.toInt() ?: 1
        offsetx = args.getOrNull(1)?.toFloat() ?: 0f
        offsety = args.getOrNull(2)?.toFloat() ?: 0f
        offsetz = args.getOrNull(3)?.toFloat() ?: 0f
        speed = args.getOrNull(4)?.toFloat() ?: 0.25f
    }

    override fun getParticleEffect(): ParticleEffect? = ParticleEffect.SWEEPATTACK

    override fun display(loc: Location) {
        ParticleEffect.SWEEPATTACK.display(offsetx, offsety, offsetz, speed, amount, loc, 50.0)
    }

}

class ColorDust : Particle("ColorDust") {
    lateinit var color: ParticleEffect.ParticleColor

    override fun init(args: Array<String>) {
        color = ParticleEffect.OrdinaryColor(args[0].toInt(), args[1].toInt(), args[2].toInt())
    }

    override fun getParticleEffect(): ParticleEffect? = ParticleEffect.REDSTONE

    override fun display(loc: Location) {
        ParticleEffect.REDSTONE.display(color, loc, 50.0)
    }
}

class Flame : Particle("Flame") {
    var amount = 1
    var offsetx = 0f
    var offsety = 0f
    var offsetz = 0f
    var speed = 0.25f
    override fun getParticleEffect(): ParticleEffect? = ParticleEffect.FLAME


    override fun init(args: Array<String>) {
        amount = args.getOrNull(0)?.toInt() ?: 1
        offsetx = args.getOrNull(1)?.toFloat() ?: 0f
        offsety = args.getOrNull(2)?.toFloat() ?: 0f
        offsetz = args.getOrNull(3)?.toFloat() ?: 0f
        speed = args.getOrNull(4)?.toFloat() ?: 0.25f
    }

    override fun display(loc: Location) {
        ParticleEffect.FLAME.display(offsetx, offsety, offsetz, speed, amount, loc, 50.0)
    }

}

class ParticleImpl : Particle("Particle") {
    lateinit var effect: ParticleEffect
    override fun getParticleEffect(): ParticleEffect? = effect
    override fun init(args: Array<String>) {
        effect = ParticleEffect.valueOf(args[0])
    }

    override fun display(loc: Location) {
        effect.display(0f, 0f, 0f, 0.25f, 1, loc, 50.0)
    }

}