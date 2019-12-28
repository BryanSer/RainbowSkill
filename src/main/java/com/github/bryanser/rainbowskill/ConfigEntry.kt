package com.github.bryanser.rainbowskill

import com.github.bryanser.rainbowskill.script.ExpressionManager
import com.github.bryanser.rainbowskill.script.ExpressionResult
import org.bukkit.Bukkit
import java.util.logging.Level

data class ConfigEntry(
        val key: String,
        val isBool: Boolean = false,
        val defaultValue: Any,
        var value: (CastData) -> ExpressionResult
) : (CastData) -> ExpressionResult {
    constructor(key: String, default: Double) : this(key, defaultValue = default, value = {
        ExpressionResult(default)
    })

    constructor(key: String, default: Boolean) : this(key, true, defaultValue = default, value ={
        ExpressionResult(default)
    })


    override fun invoke(cd: CastData): ExpressionResult = value(cd)

    fun reload(value: String) {
        try {
            val exp = ExpressionManager.compileExpression(value, isBool)
            this.value = {
                exp.invoke(it)
            }
        } catch (e: Throwable) {
            Bukkit.getLogger().log(Level.WARNING, "读取配置$key 时发生错误:", e)
        }
    }
}