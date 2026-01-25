package oluni.official.bioDistortion.extensions

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.entity.ItemDisplay
import org.bukkit.persistence.PersistentDataType

private val entityIdKey = NamespacedKey("mysticism", "block_entity_id")
private val flowers = setOf(
    Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM,
    Material.AZURE_BLUET, Material.RED_TULIP, Material.ORANGE_TULIP,
    Material.WHITE_TULIP, Material.PINK_TULIP, Material.OXEYE_DAISY,
    Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.WITHER_ROSE,
    Material.FERN, Material.DEAD_BUSH,
    Material.TORCHFLOWER, Material.PINK_PETALS, Material.MOSS_CARPET,
    Material.OAK_SAPLING, Material.SPRUCE_SAPLING, Material.BIRCH_SAPLING,
    Material.JUNGLE_SAPLING, Material.ACACIA_SAPLING, Material.DARK_OAK_SAPLING,
    Material.CHERRY_SAPLING, Material.MANGROVE_PROPAGULE, Material.BAMBOO_SAPLING,
    Material.PALE_OAK_SAPLING,
    Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.WARPED_FUNGUS,
    Material.CRIMSON_FUNGUS, Material.SPORE_BLOSSOM,
    Material.SUGAR_CANE, Material.PITCHER_POD, Material.LILY_PAD,
    Material.OPEN_EYEBLOSSOM, Material.CLOSED_EYEBLOSSOM
)

private val tallFlowers = setOf(
    Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY,
    Material.LARGE_FERN,
    Material.PITCHER_PLANT, Material.SWEET_BERRY_BUSH, Material.CAVE_VINES,
    Material.AZALEA, Material.FLOWERING_AZALEA, Material.SMALL_DRIPLEAF,
    Material.BIG_DRIPLEAF, Material.BAMBOO, Material.PUMPKIN_STEM,
    Material.MELON_STEM
)

private val grassesBlocks = setOf(
    Material.GRASS_BLOCK,
    Material.PODZOL,
    Material.DIRT_PATH,
    Material.DIRT,
    Material.COARSE_DIRT,
    Material.ROOTED_DIRT,
    Material.FARMLAND,
    Material.MUD
)
private val replaceableBlocks = setOf(
    Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL, Material.DIRT_PATH,
    Material.COARSE_DIRT, Material.ROOTED_DIRT, Material.FARMLAND, Material.MUD, Material.MOSS_BLOCK
)

fun Block.isReplaceableBlock(): Boolean = type in replaceableBlocks
fun Block.isAnomaly(): Boolean {
    val center = this.location.clone().add(0.5, 0.5, 0.5)
    return world.getNearbyEntities(center, 0.1, 0.1, 0.1)
        .filterIsInstance<ItemDisplay>()
        .any { it.persistentDataContainer.has(entityIdKey, PersistentDataType.STRING) }
}
fun Block.isFlower(): Boolean = type in flowers
fun Block.isTallFlower(): Boolean = type in tallFlowers
fun Block.isGrassBlock(): Boolean = type in grassesBlocks
fun Block.isGrass(): Boolean = type == Material.SHORT_GRASS
fun Block.isTallGrass(): Boolean = type == Material.TALL_GRASS
