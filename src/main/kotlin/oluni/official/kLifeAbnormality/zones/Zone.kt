package oluni.official.kLifeAbnormality.zones

import org.bukkit.Location

class Zone(private val zones: MutableList<Location> = mutableListOf()) {
    fun add(center: Location) = zones.add(center)
    fun getZones(): MutableList<Location> = zones
}