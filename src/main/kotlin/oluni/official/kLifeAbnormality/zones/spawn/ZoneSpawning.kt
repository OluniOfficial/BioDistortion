package oluni.official.kLifeAbnormality.zones.spawn

import oluni.official.kLifeAbnormality.extensions.*
import oluni.official.kLifeAbnormality.models.BlockEntity
import oluni.official.kLifeAbnormality.models.list.CustomBlocks
import oluni.official.kLifeAbnormality.zones.mechanic.Particles
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import kotlin.math.min
import kotlin.math.sqrt

class ZoneSpawning(val particles: Particles) {
    private val circleOffsets = (-2..2).flatMap { x ->
        (-2..2).map { z -> x to z }
    }.filter { (x, z) -> sqrt((x * x + z * z).toDouble()) <= 2.5 }

    fun findLocationNearPlayer(player: Player, attempts: Int = 0) {
        if (attempts > 5) return
        val world = player.world
        if (world.environment != World.Environment.NORMAL) return
        val randomX = player.location.blockX + (-50..50).random()
        val randomZ = player.location.blockZ + (-50..50).random()
        val startY = player.location.blockY + (-10..10).random()
        val spawnLoc = findSurface(world, randomX, startY, randomZ)
        if (spawnLoc != null) {
            createZone(spawnLoc, player, attempts)
        } else {
            findLocationNearPlayer(player, attempts + 1)
        }
    }

    fun createZone(location: Location, player: Player, attempts: Int) {
        val world = location.world ?: return
        val validLocations = mutableListOf<Location>()
        for ((xOffset, zOffset) in circleOffsets) {
            val targetX = location.blockX + xOffset
            val targetZ = location.blockZ + zOffset
            val floorBlock = findLocalSurface(world.getBlockAt(targetX, location.blockY, targetZ))
            if (floorBlock != null && floorBlock.isReplaceableBlock() && !floorBlock.isAnomaly()) {
                val plantBlock = floorBlock.getRelative(BlockFace.UP)
                when {
                    plantBlock.isFlower() -> {
                        plantBlock.type = Material.WITHER_ROSE
                    }
                    plantBlock.isTallFlower() -> {
                        val top = plantBlock.getRelative(BlockFace.UP)
                        plantBlock.type = Material.AIR
                        if (top.type == Material.TALL_GRASS || top.isTallFlower()) top.type = Material.AIR
                        BlockEntity(plantBlock.location, CustomBlocks.ANOMALY_KOREN)
                    }
                    plantBlock.isGrass() || plantBlock.type == Material.SHORT_GRASS -> {
                        plantBlock.type = Material.AIR
                        BlockEntity(plantBlock.location, CustomBlocks.ANOMALY_SHORT_GRASS)
                    }
                    plantBlock.isTallGrass() || plantBlock.type == Material.TALL_GRASS -> {
                        val top = plantBlock.getRelative(BlockFace.UP)
                        plantBlock.type = Material.AIR
                        if (top.type == Material.TALL_GRASS) top.type = Material.AIR
                        BlockEntity(plantBlock.location, CustomBlocks.ANOMALY_LONG_GRASS)
                    }
                    plantBlock.type == Material.SNOW -> plantBlock.type = Material.AIR
                }
                BlockEntity(floorBlock.location, CustomBlocks.ANOMALY_DIRT)
                validLocations += floorBlock.location
            }
        }
        if (validLocations.isNotEmpty()) {
            val raysCount = min((3..5).random(), validLocations.size)
            validLocations.shuffled().take(raysCount).forEach { rayLoc ->
                particles.greenParticlesRunnable(rayLoc.clone().add(0.5, 1.0, 0.5))
            }
            world.getNearbyEntities(location, 4.0, 4.0, 4.0).forEach { entity ->
                if (entity is LivingEntity) entity.damage(16.0)
            }
        } else {
            findLocationNearPlayer(player, attempts + 1)
        }
    }

    private fun findSurface(world: World, x: Int, y: Int, z: Int): Location? {
        for (dy in 0..20) {
            for (direction in listOf(1, -1)) {
                val currentY = y + (dy * direction)
                if (currentY !in world.minHeight..world.maxHeight) continue
                val block = world.getBlockAt(x, currentY, z)
                if (block.isReplaceableBlock()) {
                    val above = block.getRelative(BlockFace.UP)
                    if (above.type == Material.AIR || above.isFlower() || above.isGrass()) {
                        return block.location
                    }
                }
            }
        }
        return null
    }

    private fun findLocalSurface(baseBlock: Block): Block? {
        for (dy in listOf(0, 1, -1, 2, -2)) {
            val check = baseBlock.getRelative(0, dy, 0)
            if (check.isReplaceableBlock()) {
                val above = check.getRelative(BlockFace.UP)
                if (!above.type.isSolid || above.isFlower() || above.isGrass()) return check
            }
        }
        return null
    }
}