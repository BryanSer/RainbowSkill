package com.github.bryanser.rainbowskill

import com.relatev.minecraft.TRAttribute.ValueType

data class AttributeModifier(
        var type: ValueType,
        var value: Double,
        val attribute: String
)