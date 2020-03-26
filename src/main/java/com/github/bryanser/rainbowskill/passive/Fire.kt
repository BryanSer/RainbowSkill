package com.github.bryanser.rainbowskill.passive

import com.github.bryanser.rainbowskill.Passive
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

object Fire: Passive("流火"){

    val target = hashMapOf<UUID,Data>()

    data class Data(
            var target: UUID? = null,
            var times:Int = 0
    )

    @EventHandler
    fun onDamage(evt: EntityDamageByEntityEvent){
        val d = evt.damager as? Player ?: return
        val data = target[d.uniqueId] ?: return
        if(data.target == evt.entity.uniqueId){
            data.times++
            if(data.times % 3 == 0){
                evt.entity.fireTicks = 40
            }
        }else{
            data.target = evt.entity.uniqueId
            data.times = 0
        }
    }


    override fun init() {
    }

    override fun startPassive(p: Player) {
        target[p.uniqueId] = Data()
    }

    override fun stopPassive(p: Player) {
        target.remove(p.uniqueId)
    }

}