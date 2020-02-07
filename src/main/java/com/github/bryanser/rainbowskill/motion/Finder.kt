package com.github.bryanser.rainbowskill.motion

import com.github.bryanser.rainbowskill.CastData
import org.bukkit.entity.LivingEntity

interface Finder<out F> {
    fun finder(p: LivingEntity): Collection<F>
    operator fun invoke(p: CastData): Collection<F>
}