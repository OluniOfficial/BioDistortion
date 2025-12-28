package oluni.official.kLifeAbnormality.zones.mechanic

import oluni.official.kLifeAbnormality.zones.Zone
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.sqrt

class AbnormalitySpread(private val zone: Zone): BukkitRunnable() {
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
        Material.SHORT_GRASS
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
    private val circleOffsets = (-2..2).flatMap { x ->
        (-2..2).map { z -> x to z }
    }.filter { (x, z) -> sqrt((x * x + z * z).toDouble()) <= 2.5 }
    val offsets = listOf(
        1 to 0,
        -1 to 0,
        0 to 1,
        0 to -1
    )
    override fun run() {
        for (center in zone.getZones()) {
            for ((xOffset, zOffset) in circleOffsets) {
                with(center) {
                    val block = block.getRelative(xOffset, 0, zOffset)
                    if (block.type == Material.MYCELIUM) {
                        spread(block)
                    }
                }
            }
        }
    }

    fun spread(sourceBlock: Block) {
        for ((dx, dz) in offsets) {
            val target = sourceBlock.getRelative(dx, 0, dz)
            if (target.type == Material.GRASS_BLOCK) {
                target.type = Material.MYCELIUM

                val plant = target.getRelative(BlockFace.UP)
                when {
                    isFlower(plant.type) -> plant.type = Material.WITHER_ROSE
                    isTall(plant.type) -> {
                        plant.type = Material.DEAD_BUSH
                        plant.getRelative(BlockFace.UP).type = Material.AIR
                    }
                }
            }
        }
    }
    fun isFlower(material: Material): Boolean = material in flowers
    fun isTall(material: Material): Boolean = material in tallFlowers
}