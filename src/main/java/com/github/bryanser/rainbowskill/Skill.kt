package com.github.bryanser.rainbowskill

import com.github.bryanser.rainbowskill.event.SkillCastEvent
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
import com.github.bryanser.rainbowskill.impl.magician.secret.CurseOfMystery
import com.github.bryanser.rainbowskill.impl.magician.secret.MysteriousFlash
import com.github.bryanser.rainbowskill.impl.magician.thunder.AnythingThatVanishesInAFlash
import com.github.bryanser.rainbowskill.impl.magician.thunder.ElectricalWave
import com.github.bryanser.rainbowskill.impl.shooter.elf.KillingBloom
import com.github.bryanser.rainbowskill.impl.shooter.elf.PiercingArrow
import com.github.bryanser.rainbowskill.impl.shooter.elf.Scattering
import com.github.bryanser.rainbowskill.impl.shooter.hunter.BlastingArrow
import com.github.bryanser.rainbowskill.impl.shooter.knighterrant.ExplosiveFire
import com.github.bryanser.rainbowskill.impl.warrior.giantsword.AdmirablyWonderful
import com.github.bryanser.rainbowskill.impl.warrior.giantsword.TigerHeart
import com.relatev.minecraft.RainbowHero.skill.CastResultKey
import com.relatev.minecraft.RainbowHero.skill.Castable
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.*

abstract class Skill(
        val _name: String,
        internal val description: MutableList<String>,
        internal val displayMaterial: Material,
        internal val configs: List<ConfigEntry>
) : Castable {

    override fun getName(): String = _name

    val file: File = File(folder, "$name.yml")

    val cooldown: ConfigEntry? by lazy {
        for (cfg in configs) {
            if (cfg.key == COOLDOWN_KEY) {
                return@lazy cfg
            }
        }
        null
    }

    protected fun getConfigEntry(key: String): ConfigEntry {
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

    override fun cast(player: Player, level: Int): EnumMap<CastResultKey, Any?> {
        val cd = CastData(player, level)
        val last = lastCast[player.name] ?: 0L
        val cool = getCooldown(cd)
        val map = EnumMap<CastResultKey, Any?>(CastResultKey::class.java)
        if (cool > 0) {
            val pass = System.currentTimeMillis() - last
            if (pass < cool * 1000) {
                val has = cool * 1000 - pass
                map[CastResultKey.REMAIN_COOLDOWN_MILLIONSECOND] = has
                return map
            }
        }
        val evt = SkillCastEvent(player, this, cd)
        Bukkit.getPluginManager().callEvent(evt)
        if (evt.isCancelled) {
            map[CastResultKey.CAST_RESULT] = false
            map[CastResultKey.FAIL_CUSTOM_MESSAGE] = evt.message
            return map
        }
        if (this.onCast(cd)) {
            map[CastResultKey.REMAIN_COOLDOWN_MILLIONSECOND] = cool * 1000
        }
        map[CastResultKey.CAST_RESULT] = true
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

        val skills = mutableMapOf<String, Skill>()

        fun registerSkill(ski: Skill) {
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
            registerSkill(Scattering)
            registerSkill(PiercingArrow)

            registerSkill(KillingBloom)
            registerSkill(ExplosiveFire)
            registerSkill(ElectricalWave)
            registerSkill(BlastingArrow)
            registerSkill(AdmirablyWonderful)
            registerSkill(CurseOfMystery)
            registerSkill(AnythingThatVanishesInAFlash)
        }
    }
}

