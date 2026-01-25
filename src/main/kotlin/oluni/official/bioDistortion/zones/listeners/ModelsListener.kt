package oluni.official.bioDistortion.zones.listeners

import oluni.official.bioDistortion.extensions.entityIdKey
import oluni.official.bioDistortion.models.BlockEntity
import oluni.official.bioDistortion.models.list.CustomBlocks
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.ItemDisplay
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class ModelsListener: Listener {
    private val blockIdKey = NamespacedKey("mysticism", "block_id")

    @EventHandler
    fun onBlockPlaced(event: BlockPlaceEvent) {
        val item = event.itemInHand
        val meta = item.itemMeta ?: return
        val id = meta.persistentDataContainer.get(blockIdKey, PersistentDataType.STRING) ?: return
        val customBlock = when (id) {
            CustomBlocks.ANOMALY_DIRT.id -> CustomBlocks.ANOMALY_DIRT
            CustomBlocks.ANOMALY_SHORT_GRASS.id -> CustomBlocks.ANOMALY_SHORT_GRASS
            CustomBlocks.ANOMALY_LONG_GRASS.id -> CustomBlocks.ANOMALY_LONG_GRASS
            CustomBlocks.ANOMALY_KOREN.id -> CustomBlocks.ANOMALY_KOREN
            else -> null
        } ?: return
        if (customBlock.isTall && event.blockPlaced.getRelative(0, 1, 0).type != Material.AIR) {
            event.isCancelled = true
            return
        }
        BlockEntity(event.blockPlaced.location, customBlock)
    }

    @EventHandler
    fun onBarrierDamage(event: PlayerInteractEvent) {
        if (event.action != Action.LEFT_CLICK_BLOCK) return
        val block = event.clickedBlock ?: return
        if (event.player.gameMode == GameMode.CREATIVE) return
        if (block.type == Material.BARRIER) {
            val display = findDisplay(block) ?: findDisplay(block.getRelative(0, -1, 0))
            if (display != null) {
                val breakEvent = BlockBreakEvent(block, event.player)
                Bukkit.getPluginManager().callEvent(breakEvent)
            }
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        val isBarrier = block.type == Material.BARRIER
        val isGrass = block.type == Material.GRASS_BLOCK
        if (!isBarrier && !isGrass) return
        val display = if (isBarrier) {
            findDisplay(block) ?: findDisplay(block.getRelative(0, -1, 0))
        } else {
            findDisplay(block)
        } ?: return
        val id = display.persistentDataContainer.get(entityIdKey, PersistentDataType.STRING) ?: return
        val displayLoc = display.location.block
        display.remove()
        if (id != CustomBlocks.ANOMALY_DIRT.id) {
            event.isCancelled = true
            block.world.playSound(block.location, Sound.BLOCK_GRASS_BREAK, 1f, 1.2f)

            displayLoc.type = Material.AIR
            if (id == CustomBlocks.ANOMALY_KOREN.id || id == CustomBlocks.ANOMALY_LONG_GRASS.id) {
                displayLoc.getRelative(0, 1, 0).type = Material.AIR
            }
        } else {
            event.isDropItems = false
        }
        val tool = event.player.inventory.itemInMainHand
        val hasSilk = tool.itemMeta?.hasEnchant(Enchantment.SILK_TOUCH) == true
        val customBlock = when(id) {
            CustomBlocks.ANOMALY_DIRT.id -> CustomBlocks.ANOMALY_DIRT
            CustomBlocks.ANOMALY_SHORT_GRASS.id -> CustomBlocks.ANOMALY_SHORT_GRASS
            CustomBlocks.ANOMALY_LONG_GRASS.id -> CustomBlocks.ANOMALY_LONG_GRASS
            CustomBlocks.ANOMALY_KOREN.id -> CustomBlocks.ANOMALY_KOREN
            else -> null
        }
        if (customBlock != null) {
            val shouldDrop = when(id) {
                CustomBlocks.ANOMALY_DIRT.id -> tool.type.name.contains("SHOVEL") && hasSilk
                CustomBlocks.ANOMALY_KOREN.id -> tool.type.name.contains("AXE") && hasSilk
                CustomBlocks.ANOMALY_SHORT_GRASS.id, CustomBlocks.ANOMALY_LONG_GRASS.id -> tool.type == Material.SHEARS
                else -> false
            }
            if (shouldDrop) {
                block.world.dropItemNaturally(displayLoc.location, customBlock.getItem(1))
            }
        }
    }

    @EventHandler
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        val block = event.block
        val above = block.getRelative(0, 1, 0)
        if (block.type == Material.AIR) {
            val display = findDisplay(above) ?: return
            val id = display.persistentDataContainer.get(entityIdKey, PersistentDataType.STRING) ?: return
            if (above.type == Material.BARRIER) {
                display.remove()
                above.type = Material.AIR
                if (id == CustomBlocks.ANOMALY_KOREN.id || id == CustomBlocks.ANOMALY_LONG_GRASS.id) {
                    above.getRelative(0, 1, 0).type = Material.AIR
                }
            }
        }
    }

    private fun findDisplay(block: Block): ItemDisplay? {
        return block.world.getNearbyEntities(block.location.add(0.5, 0.5, 0.5), 0.1, 0.1, 0.1)
            .filterIsInstance<ItemDisplay>()
            .find { it.persistentDataContainer.has(entityIdKey, PersistentDataType.STRING) }
    }
}