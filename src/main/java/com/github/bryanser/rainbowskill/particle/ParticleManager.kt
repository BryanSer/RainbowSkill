package com.github.bryanser.rainbowskill.particle

object ParticleManager {
    private val particle = mutableMapOf<String, Class<out Particle>>()

    fun init() {
        register("ColorDust", ColorDust::class.java)
        register("Flame", Flame::class.java)
        register("SweepAttack", SweepAttack::class.java)
        register("Particle", ParticleImpl::class.java)
    }

    fun register(name: String, clazz: Class<out Particle>) {
        particle[name] = clazz
    }

    fun readParticle(str: String): Particle {
        val sp = str.split("[()]".toRegex(), 2)
        val clazz = particle[sp[0]] ?: throw IllegalArgumentException("找不到名为${sp[0]}的Particle")
        val p = clazz.newInstance()
        try {
            p.init(sp[1].replace(")", "").split(",").toTypedArray())
        } catch (e: Throwable) {
        }
        return p
    }
}