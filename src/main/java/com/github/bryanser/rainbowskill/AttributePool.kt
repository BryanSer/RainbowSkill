package com.github.bryanser.rainbowskill

import com.relatev.minecraft.TRAttribute.AttributeValue
import com.relatev.minecraft.TRAttribute.TRAttribute
import org.bukkit.entity.Player

class AttributePool(
        val owner: String,
        val data: HashMap<String, AttributeModifier> = hashMapOf()
) : MutableMap<String, AttributeModifier> by data {
    fun update(p: Player) {
        if (p.name != owner) {
            throw IllegalArgumentException("属性持有者不是更新的对象")
        }
        val map = hashMapOf<String, MutableList<AttributeValue>>()
        for ((k, v) in this) {
            map.getOrPut(k) {
                mutableListOf()
            }.add(AttributeValue(
                    v.type,
                    v.value
            ))
        }
        for ((k, v) in map) {
            TRAttribute.MainPlugin.attributeManager.enabledAttributes[k]?.setValues(
                    p,
                    v,
                    Main.Plugin
            )
        }
    }
}
