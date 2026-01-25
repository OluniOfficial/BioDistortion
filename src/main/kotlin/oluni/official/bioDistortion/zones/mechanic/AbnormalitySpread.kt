package oluni.official.bioDistortion.zones.mechanic

import oluni.official.bioDistortion.extensions.entityIdKey
import oluni.official.bioDistortion.extensions.isAnomaly
import oluni.official.bioDistortion.extensions.isFlower
import oluni.official.bioDistortion.extensions.isGrass
import oluni.official.bioDistortion.extensions.isGrassBlock
import oluni.official.bioDistortion.extensions.isTallFlower
import oluni.official.bioDistortion.extensions.isTallGrass
import oluni.official.bioDistortion.models.BlockEntity
import oluni.official.bioDistortion.models.list.CustomBlocks
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.ItemDisplay
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable

class AbnormalitySpread : BukkitRunnable() {
    private val offsets = listOf(
        BlockFace.NORTH, BlockFace.SOUTH,
        BlockFace.EAST, BlockFace.WEST
    )

    override fun run() {
        for (world in Bukkit.getWorlds()) {
            for (entity in world.entities) {
                if (entity !is ItemDisplay) continue
                val id = entity.persistentDataContainer.get(entityIdKey, PersistentDataType.STRING)
                if (id == CustomBlocks.ANOMALY_DIRT.id) {
                    val sourceBlock = entity.location.block
                    spread(sourceBlock)
                }
            }
        }
    }

    private fun spread(source: Block) {
        for (face in offsets) {
            val target = source.getRelative(face)
            if (target.isGrassBlock() && !target.isAnomaly()) {
                BlockEntity(target.location, CustomBlocks.ANOMALY_DIRT)
                val plant = target.getRelative(BlockFace.UP)
                when {
                    plant.isFlower() -> {
                        plant.type = Material.AIR
                        BlockEntity(plant.location, CustomBlocks.ANOMALY_SHORT_GRASS)
                    }
                    plant.isTallFlower() -> {
                        val top = plant.getRelative(BlockFace.UP)
                        plant.type = Material.AIR
                        if (top.isTallFlower()) {
                            top.type = Material.AIR
                        }
                        BlockEntity(plant.location, CustomBlocks.ANOMALY_KOREN)
                    }
                    plant.isGrass() -> {
                        plant.type = Material.AIR
                        BlockEntity(plant.location, CustomBlocks.ANOMALY_SHORT_GRASS)
                    }
                    plant.isTallGrass() -> {
                        val top = plant.getRelative(BlockFace.UP)
                        plant.type = Material.AIR
                        if (top.isTallFlower()) {
                            top.type = Material.AIR
                        }
                        BlockEntity(plant.location, CustomBlocks.ANOMALY_LONG_GRASS)
                    }
                }
            }
        }
    }
}