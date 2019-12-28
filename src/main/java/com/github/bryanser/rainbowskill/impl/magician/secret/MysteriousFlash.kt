package com.github.bryanser.rainbowskill.impl.magician.secret

import com.github.bryanser.rainbowskill.Skill
import com.github.bryanser.rainbowskill.impl.Motion
import com.github.bryanser.rainbowskill.impl.SkillUtils
import com.github.bryanser.rainbowskill.script.ExpressionResult
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.*


//向前方5格闪现
class MysteriousFlash : Skill("奥秘闪现", mutableListOf(""), Material.REDSTONE) {

    override fun onCast(player: Player, level: Int): EnumMap<CastResultType, Any> {

//        val vec: Vector = player.location.direction.normalize()
//
//        var point = player.location
//        for(i in 1..5){
//            if (point.block.type == Material.AIR){
//                point = point.add(vec).multiply(1.0)
//            } else {
//                break
//            }
//        }
//
//        player.teleport(point)
        Motion.jump(player,1,5.0)

        val map = EnumMap<CastResultType, Any>(CastResultType::class.java)
        return map
    }

}