package oluni.official.bioDistortion.zones.listeners

import oluni.official.bioDistortion.extensions.isAnomaly
import oluni.official.bioDistortion.extensions.isFlower
import oluni.official.bioDistortion.extensions.isGrass
import oluni.official.bioDistortion.extensions.isTallFlower
import oluni.official.bioDistortion.extensions.isTallGrass
import oluni.official.bioDistortion.models.BlockEntity
import oluni.official.bioDistortion.models.list.CustomBlocks
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class FlowerPlace: Listener {

    @EventHandler
    fun placeFlowerOnAnomalyDirt(event: BlockPlaceEvent) {
        val block = event.block
        if (block.getRelative(0, -1, 0).isAnomaly()) {
            when {
                block.isFlower() -> {
                    block.type = Material.WITHER_ROSE
                }
                block.isTallFlower() -> {
                    val top = block.getRelative(BlockFace.UP)
                    block.type = Material.AIR
                    if (top.isTallFlower()) {
                        top.type = Material.AIR
                    }
                    BlockEntity(block.location, CustomBlocks.ANOMALY_KOREN)
                }
                block.isGrass() -> {
                    block.type = Material.AIR
                    BlockEntity(block.location, CustomBlocks.ANOMALY_SHORT_GRASS)
                }
                block.isTallGrass() -> {
                    val top = block.getRelative(BlockFace.UP)
                    block.type = Material.AIR
                    if (top.isTallFlower()) {
                        top.type = Material.AIR
                    }
                    BlockEntity(block.location, CustomBlocks.ANOMALY_LONG_GRASS)
                }
            }
        }
    }
}