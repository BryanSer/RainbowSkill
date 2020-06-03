package com.github.bryanser.rainbowskill

import org.bukkit.entity.Player

class AttributePool(
        val owner: String,
        val data: HashMap<String, MutableList<AttributeModifier>> = hashMapOf()
) : MutableMap<String, MutableList<AttributeModifier>> by data {


//    init{
//        for(k in TRAttribute.MainPlugin.attributeManager.enabledAttributes.keys){
//            data[k] = mutableListOf()
//        }
//    }

    fun update(p: Player) {
        if (p.name != owner) {
            throw IllegalArgumentException("属性持有者不是更新的对象")
        }
//        val map = hashMapOf<String, MutableList<AttributeValue>>()
//        for ((k, v) in this) {
//            map.getOrPut(k) {
//                mutableListOf()
//            }.addAll(v.map { v->
//                AttributeValue(
//                        v.type,
//                        v.value
//                )
//            })
//        }
//        for ((k, v) in map) {
//            TRAttribute.MainPlugin.attributeManager.enabledAttributes[k]?.setValues(
//                    p,
//                    v,
//                    Main.Plugin
//            )
//        }
    }
}
