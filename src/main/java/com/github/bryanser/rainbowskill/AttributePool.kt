package com.github.bryanser.rainbowskill

import com.github.bryanser.rainbowskill.script.ExpressionResult
import com.relatev.minecraft.TRAttribute.AttributeValue
import com.sun.org.apache.xpath.internal.operations.Bool
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
            AttributeManager.manager.setAttribute(p, k, v, Main.Plugin)
        }
    }
}
