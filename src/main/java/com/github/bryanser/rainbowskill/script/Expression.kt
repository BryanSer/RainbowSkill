package com.github.bryanser.rainbowskill.script

import com.github.bryanser.rainbowskill.CastData
import org.bukkit.entity.Player

interface Expression : (CastData) -> ExpressionResult
typealias VarReader = (Player, String) -> String?


