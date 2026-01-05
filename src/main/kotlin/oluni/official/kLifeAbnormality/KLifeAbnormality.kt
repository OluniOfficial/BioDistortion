package oluni.official.kLifeAbnormality

import oluni.official.kLifeAbnormality.zones.listeners.FlowerPlace
import oluni.official.kLifeAbnormality.zones.listeners.ModelsListener
import oluni.official.kLifeAbnormality.zones.mechanic.AbnormalitySpread
import oluni.official.kLifeAbnormality.zones.mechanic.Particles
import oluni.official.kLifeAbnormality.zones.spawn.ZoneRunnable
import oluni.official.kLifeAbnormality.zones.spawn.ZoneSpawning
import org.bukkit.plugin.java.JavaPlugin

class KLifeAbnormality : JavaPlugin() {

    override fun onEnable() {
        val particle = Particles(this)
        val zoneSpawning = ZoneSpawning(particle)
        val zoneRunnable = ZoneRunnable(zoneSpawning)
        val abnormalitySpread = AbnormalitySpread()
        zoneRunnable.runTaskTimer(this, 0L, 1200L)
        server.pluginManager.registerEvents(ModelsListener(), this)
        server.pluginManager.registerEvents(FlowerPlace(), this)
        abnormalitySpread.runTaskTimer(this, 10000L, 10000L)
    }
}
