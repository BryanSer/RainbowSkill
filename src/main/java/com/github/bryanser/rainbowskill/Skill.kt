package com.github.bryanser.rainbowskill

import com.github.bryanser.rainbowskill.impl.assassin.dagger.DeciduousLeafCutting
import com.github.bryanser.rainbowskill.impl.assassin.dagger.FlyingLeaf
import com.github.bryanser.rainbowskill.impl.idleman.BouquetOfTheGodOfFire
import com.github.bryanser.rainbowskill.impl.idleman.FallenPalm
import com.github.bryanser.rainbowskill.impl.knighterrant.sword.GaleAndFlyingSword
import com.github.bryanser.rainbowskill.impl.magician.fire.FireBall
import com.github.bryanser.rainbowskill.impl.magician.fire.FireWall
import com.github.bryanser.rainbowskill.impl.magician.lcyice.IceDragon
import com.github.bryanser.rainbowskill.impl.magician.lcyice.IceSpirit
import com.github.bryanser.rainbowskill.impl.magician.lcyice.IceWall
import com.github.bryanser.rainbowskill.impl.magician.secret.MysteriousFlash
import com.github.bryanser.rainbowskill.impl.warrior.giantsword.TigerHeart
import com.relatev.minecraft.RainbowHero.skill.CastResultType
import com.relatev.minecraft.RainbowHero.skill.Castable
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.*

abstract class Skill(
        val name: String,
        internal val description: MutableList<String>,
        internal val displayMaterial: Material,
        internal val configs: List<ConfigEntry>
) : Castable {

    val file: File = File(folder, "$name.yml")

    val cooldown: ConfigEntry? by lazy {
        for (cfg in configs) {
            if (cfg.key == COOLDOWN_KEY) {
                return@lazy cfg
            }
        }
        null
    }

    protected fun getConfigEntry(key:String):ConfigEntry{
        for (cfg in this.configs) {
            if (cfg.key == key) {
                return cfg
            }
        }
        throw IllegalStateException()
    }

    open fun loadConfig() {
        if (!file.exists()) {
            val config = YamlConfiguration()
            for (cfg in configs) {
                config.set(cfg.key, cfg.defaultValue)
            }
            config.save(file)
        } else {
            val config = YamlConfiguration.loadConfiguration(file)
            for (cfg in configs) {
                val str = config.getString(cfg.key)
                cfg.reload(str)
            }
        }
    }

    protected val lastCast = hashMapOf<String, Long>()

    abstract fun onCast(cd: CastData): Boolean

    override fun cast(player: Player, level: Int): EnumMap<CastResultType, Any?> {
        val cd = CastData(player, level)
        val last = lastCast[player.name] ?: 0L
        val cool = getCooldown(cd)
        val map = EnumMap<CastResultType,Any?>(CastResultType::class.java)
        if(cool > 0){
            val pass = System.currentTimeMillis() - last
            if(pass < cool * 1000){
                val has = cool * 1000 - pass
                map[CastResultType.COOLDOWN] = has
                return map
            }
        }
        if(this.onCast(cd)){
            map[CastResultType.COOLDOWN] = cool * 1000
        }
        return map
    }

    open fun getCooldown(cd: CastData): Double {
        if (cooldown == null) {
            return 0.0
        }
        return cooldown!!.invoke(cd).toDouble()
    }

    override fun getDescription(): MutableList<String> = description

    override fun getDisplayMaterial(): Material = displayMaterial

    companion object {

        val skills = mutableMapOf<String,Skill>()

        fun registerSkill(ski:Skill){
            skills[ski.name] = ski
            ski.loadConfig()
        }

        const val COOLDOWN_KEY = "Cooldown"

        val folder = File(Main.Plugin.dataFolder, "/Skills/")

        init {
            if (!folder.exists()) {
                folder.mkdirs()
            }
            //TODO()
            registerSkill(FlyingLeaf)
            registerSkill(DeciduousLeafCutting)
            registerSkill(GaleAndFlyingSword)
            registerSkill(MysteriousFlash)
            registerSkill(FireWall)
            registerSkill(FallenPalm)
            registerSkill(FireBall)
            registerSkill(TigerHeart)
            registerSkill(IceWall)
            registerSkill(BouquetOfTheGodOfFire)
            registerSkill(IceSpirit)
            registerSkill(IceDragon)

        }
    }
}

