package oluni.official.bioDistortion.models

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

data class BlockItem(
    val id: String,
    val displayName: String,
    val lore: List<String>,
    val modelPath: String,
    val hitboxMaterial: Material,
    val isTall: Boolean = false
) {
    fun getItem(amount: Int): ItemStack {
        val item = ItemStack(Material.BARRIER, amount)
        val meta = item.itemMeta ?: return item
        meta.setDisplayName(displayName)
        meta.lore = lore
        meta.persistentDataContainer.set(
            NamespacedKey("mysticism", "block_id"),
            PersistentDataType.STRING,
            id
        )
        val parts = modelPath.split(":")
        if (parts.size == 2) meta.itemModel = NamespacedKey(parts[0], parts[1])
        item.itemMeta = meta
        return item
    }
}