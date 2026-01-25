package oluni.official.bioDistortion.models.list

import net.kyori.adventure.text.Component
import oluni.official.bioDistortion.models.BlockItem
import org.bukkit.Material

object CustomBlocks {
    val ANOMALY_DIRT = BlockItem(
        id = "n_dirt",
        displayName = Component.text("§r§fДушегубительный дёрн"),
        lore = listOf(Component.text("§7§oАномальная земля")),
        modelPath = "mysticism:block/n_dirn",
        hitboxMaterial = Material.GRASS_BLOCK
    )

    val ANOMALY_SHORT_GRASS = BlockItem(
        id = "n_trawa",
        displayName = Component.text("§r§fДушегубительная трава"),
        lore = listOf(Component.text("§7§oАномальная трава")),
        modelPath = "mysticism:block/n_trawa",
        hitboxMaterial = Material.BARRIER
    )

    val ANOMALY_LONG_GRASS = BlockItem(
        id = "n_grass_long",
        displayName = Component.text("§r§fДушегубительная трава"),
        lore = listOf(Component.text("§7§oАномальная трава")),
        modelPath = "mysticism:block/n_grass_long",
        hitboxMaterial = Material.BARRIER,
        isTall = true
    )

    val ANOMALY_KOREN = BlockItem(
        id = "n_koren",
        displayName = Component.text("§r§fДушегубительный корень"),
        lore = listOf(Component.text("§7§oАномальный корень")),
        modelPath = "mysticism:block/n_koren",
        hitboxMaterial = Material.BARRIER,
        isTall = true
    )
}