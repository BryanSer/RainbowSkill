package com.github.bryanser.rainbowskill

import com.github.bryanser.brapi.Utils
import com.relatev.minecraft.TRAttribute.TRAttribute
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object AttributeManager {

    val manager = TRAttribute.MainPlugin.attributeManager

    val modifyAttribute = hashMapOf<String, AttributePool>()

    operator fun get(p: Player): AttributePool = modifyAttribute.getOrPut(p.name) { AttributePool(p.name) }

    operator fun get(p: String): AttributePool = modifyAttribute.getOrPut(p) { AttributePool(p) }

    init{
        Bukkit.getScheduler().runTaskTimer(Main.Plugin,{
            for(p in Utils.getOnlinePlayers()){
                this[p].update(p)
            }
        },20,20)
    }
}

