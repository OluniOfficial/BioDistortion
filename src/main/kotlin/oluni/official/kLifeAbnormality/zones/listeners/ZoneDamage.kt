package oluni.official.kLifeAbnormality.zones.listeners

import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import java.util.UUID

class ZoneDamage: Listener {
    private val lastDamage = hashMapOf<UUID, Long>()
    @EventHandler
    fun onEntityMoveEvent(event: EntityMoveEvent) {
        val entity = event.entity
        if (event.from.blockX == event.to.blockX && event.from.blockZ == event.to.blockZ) return
        if (entity.location.block.getRelative(BlockFace.DOWN).type == Material.MYCELIUM) {
            val currentTime = System.currentTimeMillis()
            val lastHit = lastDamage.getOrDefault(entity.uniqueId, 0L)
            if (currentTime - lastHit < 500) return
            lastDamage[entity.uniqueId] = currentTime
            entity.damage(2.0)
        }
    }
    @EventHandler
    fun onDeath(event: EntityDeathEvent) {
        lastDamage.remove(event.entity.uniqueId)
    }
}