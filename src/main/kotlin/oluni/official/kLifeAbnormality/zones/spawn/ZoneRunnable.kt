package oluni.official.kLifeAbnormality.zones.spawn

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class ZoneRunnable(private val zoneSpawning: ZoneSpawning): BukkitRunnable() {
    override fun run() {
        Bukkit.getOnlinePlayers().firstOrNull()?.let { player ->
            zoneSpawning.findLocationNearPlayer(player) }
    }
}