package oluni.official.bioDistortion.models.list

import oluni.official.bioDistortion.models.BlockItem
import org.bukkit.Material

object CustomBlocks {
    val ANOMALY_DIRT = BlockItem(
        id = "n_dirt",
        displayName = "§r§fДушегубительный дёрн",
        lore = listOf("§7§oАномальная земля"),
        modelPath = "mysticism:block/n_dirn",
        hitboxMaterial = Material.GRASS_BLOCK
    )

    val ANOMALY_SHORT_GRASS = BlockItem(
        id = "n_trawa",
        displayName = "§r§fДушегубительная трава",
        lore = listOf("§7§oАномальная трава"),
        modelPath = "mysticism:block/n_trawa",
        hitboxMaterial = Material.BARRIER
    )

    val ANOMALY_LONG_GRASS = BlockItem(
        id = "n_grass_long",
        displayName = "§r§fДушегубительная трава",
        lore = listOf("§7§oАномальная трава"),
        modelPath = "mysticism:block/n_grass_long",
        hitboxMaterial = Material.BARRIER,
        isTall = true
    )

    val ANOMALY_KOREN = BlockItem(
        id = "n_koren",
        displayName = "§r§fДушегубительный корень",
        lore = listOf("§7§oАномальный корень"),
        modelPath = "mysticism:block/n_koren",
        hitboxMaterial = Material.BARRIER,
        isTall = true
    )
}