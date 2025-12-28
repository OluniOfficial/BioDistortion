package oluni.official.kLifeAbnormality.zones.spawn

import oluni.official.kLifeAbnormality.zones.Zone
import oluni.official.kLifeAbnormality.zones.mechanic.Particles
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import kotlin.math.min
import kotlin.math.sqrt

class ZoneSpawning(val particles: Particles, val zone: Zone) {
    private val flowers = setOf(
        Material.DANDELION,
        Material.POPPY,
        Material.BLUE_ORCHID,
        Material.ALLIUM,
        Material.AZURE_BLUET,
        Material.RED_TULIP,
        Material.ORANGE_TULIP,
        Material.WHITE_TULIP,
        Material.PINK_TULIP,
        Material.OXEYE_DAISY,
        Material.CORNFLOWER,
        Material.LILY_OF_THE_VALLEY,
        Material.TORCHFLOWER,
        Material.WITHER_ROSE,
        Material.SHORT_GRASS,
        Material.WILDFLOWERS,
        Material.LEAF_LITTER,
        Material.FERN,
    )
    private val tallFlowers = setOf(
        Material.SUNFLOWER,
        Material.LILAC,
        Material.ROSE_BUSH,
        Material.PEONY,
        Material.TALL_GRASS,
        Material.LARGE_FERN,
        Material.PITCHER_PLANT,
        Material.SWEET_BERRY_BUSH,
        Material.AZALEA,
        Material.FLOWERING_AZALEA
    )
    private val grasses = setOf(
        Material.GRASS_BLOCK,
        Material.PODZOL,
        Material.DIRT_PATH,
        Material.DIRT,
        Material.COARSE_DIRT,
        Material.ROOTED_DIRT,
        Material.FARMLAND,
        Material.MUD
    )
    private val circleOffsets = (-2..2).flatMap { x ->
        (-2..2).map { z -> x to z }
    }.filter { (x, z) -> sqrt((x * x + z * z).toDouble()) <= 2.5 }

    fun findLocationNearPlayer(player: Player) {
        val location: Location = player.location
        val world = location.world ?: return
        if (world.environment != World.Environment.NORMAL) return
        val x = location.blockX + (-50..50).random()
        val z = location.blockZ + (-50..50).random()
        val y = world.getHighestBlockYAt(x, z)
        createZone(Location(location.world, x.toDouble(), y.toDouble(), z.toDouble()))
    }

    fun createZone(location: Location) {
        val world = location.world ?: return
        var badBlockCount = 0
        for ((xOffset, zOffset) in circleOffsets) {
            val checkLocation =
                Location(world, location.x + xOffset + 0.5, location.y + 1, location.z + zOffset + 0.5)
            world.getNearbyLivingEntities(checkLocation, 0.7).forEach { it.damage(20.0) }
            val material: Material =
                world.getBlockAt(location.blockX + xOffset, location.blockY, location.blockZ + zOffset).type
            if (material == Material.AIR || material == Material.WATER || material == Material.LAVA) {
                badBlockCount++
                if (badBlockCount > 3) {
                    Bukkit.getOnlinePlayers().firstOrNull()?.let { findLocationNearPlayer(it) }
                    return
                }
            }
            for (checkY in 1..2) {
                val anotherMaterial = world.getBlockAt(
                    location.blockX + xOffset,
                    location.blockY + checkY,
                    location.blockZ + zOffset
                ).type
                if (anotherMaterial.isSolid && !isFlower(anotherMaterial) && !isTall(anotherMaterial)) {
                    Bukkit.getOnlinePlayers().firstOrNull()?.let { findLocationNearPlayer(it) }
                    return
                }
            }
        }
        val validLocation = mutableListOf<Location>()
        for ((xOffset, zOffset) in circleOffsets) {
            validLocation += location.clone().add(xOffset.toDouble(), 0.0, zOffset.toDouble())
            with (location) {
                val floorBlock = block.getRelative(xOffset, 0, zOffset)
                if (floorBlock.type in grasses) {
                    val plantBlock =
                        block.getRelative(xOffset, 1, zOffset)
                    val plantType = plantBlock.type
                    when {
                        isFlower(plantType) -> {
                            plantBlock.type = Material.WITHER_ROSE
                        }

                        isTall(plantType) -> {
                            plantBlock.type = Material.DEAD_BUSH
                            block.getRelative(
                                xOffset,
                                2,
                                zOffset
                            ).type =
                                Material.AIR
                        }

                        plantType == Material.SNOW -> plantBlock.type = Material.AIR
                    }
                }
                floorBlock.type = Material.MYCELIUM
            }
        }
        val raysCount = min((3..5).random(), validLocation.size)
        validLocation.shuffled().take(raysCount).forEach {  rayLocation -> particles.greenParticlesRunnable(rayLocation) }
        zone.add(location)
        // TODO Убрать эту функцию. Она лишь на тестовый период.
        Bukkit.getOnlinePlayers()
            .forEach { it.sendMessage("§aЗона появилась: §f${location.blockX}, ${location.blockY}, ${location.blockZ}") }
    }

    fun isFlower(material: Material): Boolean = material in flowers
    fun isTall(material: Material): Boolean = material in tallFlowers
}