package oluni.official.kLifeAbnormality.zones.listeners

import oluni.official.kLifeAbnormality.zones.spawn.ZoneSpawning
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

/*
TODO Удалить этот класс. Он нужен только для тестовой роботы с плагином.
 */
class Testing(private val zoneSpawning: ZoneSpawning): Listener {

    @EventHandler
    fun testingZoneInteractStickEvent(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
        val player = event.player
        if (player.inventory.itemInMainHand.type != Material.STICK) return
        event.isCancelled = true
        zoneSpawning.createZone(Location(player.world, player.location.x, player.location.y - 1, player.location.z))
    }

}