package oluni.official.kLifeAbnormality

import oluni.official.kLifeAbnormality.zones.Zone
import oluni.official.kLifeAbnormality.zones.listeners.Testing
import oluni.official.kLifeAbnormality.zones.listeners.ZoneDamage
import oluni.official.kLifeAbnormality.zones.mechanic.AbnormalitySpread
import oluni.official.kLifeAbnormality.zones.mechanic.Particles
import oluni.official.kLifeAbnormality.zones.spawn.ZoneRunnable
import oluni.official.kLifeAbnormality.zones.spawn.ZoneSpawning
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class KLifeAbnormality : JavaPlugin() {

    override fun onEnable() {
        val zone = Zone()
        val particle = Particles(this)
        val zoneSpawning = ZoneSpawning(particle, zone)
        val zoneRunnable = ZoneRunnable(zoneSpawning)
        val abnormalitySpread = AbnormalitySpread(zone)
        zoneRunnable.runTaskTimer(this, 0L, 1200L)
        Bukkit.getPluginManager().registerEvents(ZoneDamage(), this)
        Bukkit.getPluginManager().registerEvents(Testing(zoneSpawning), this)
        abnormalitySpread.runTaskTimer(this, 24000L, 24000L)
    }
}
