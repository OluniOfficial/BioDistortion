package oluni.official.bioDistortion

import oluni.official.bioDistortion.zones.listeners.FlowerPlace
import oluni.official.bioDistortion.zones.listeners.ModelsListener
import oluni.official.bioDistortion.zones.mechanic.AbnormalitySpread
import oluni.official.bioDistortion.zones.mechanic.Particles
import oluni.official.bioDistortion.zones.spawn.ZoneRunnable
import oluni.official.bioDistortion.zones.spawn.ZoneSpawning
import org.bukkit.plugin.java.JavaPlugin

class BioDistortion : JavaPlugin() {

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
