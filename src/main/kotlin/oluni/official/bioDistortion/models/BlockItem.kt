package oluni.official.bioDistortion.models

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

data class BlockItem(
    val id: String,
    val displayName: Component,
    val lore: List<Component> = emptyList(),
    val modelPath: String,
    val hitboxMaterial: Material,
    val isTall: Boolean = false
) {
    fun getItem(amount: Int): ItemStack {
        val item = ItemStack(Material.BARRIER, amount)
        item.editMeta { meta ->
            meta.displayName(displayName.decoration(TextDecoration.ITALIC, false))
            meta.lore(lore.map { it.decoration(TextDecoration.ITALIC, false) })
            meta.persistentDataContainer.set(
                NamespacedKey("mysticism", "block_id"),
                PersistentDataType.STRING,
                id
            )
            NamespacedKey.fromString(modelPath)?.let {
                meta.itemModel = it
            }
        }
        return item
    }
}