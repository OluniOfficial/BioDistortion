package oluni.official.bioDistortion.models

import oluni.official.bioDistortion.extensions.entityIdKey
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ItemDisplay
import org.bukkit.persistence.PersistentDataType

class BlockEntity(private var location: Location,
                  private var blockItem: BlockItem
) {
    private lateinit var entity: ItemDisplay

    init {
        place()
    }

    private fun place() {
        val world = location.world ?: return
        val block = location.block
        block.setBlockData(blockItem.hitboxMaterial.createBlockData(), true)
        block.state.update(true, true)
        if (blockItem.isTall) {
            val upperLocation = location.clone().add(0.0, 1.0, 0.0)
            val upperBlock = upperLocation.block
            upperBlock.setBlockData(Material.BARRIER.createBlockData(), true)
            upperBlock.state.update(true, true)
        }
        val entityLoc = location.clone().add(0.5, 0.5, 0.5)
        world.spawn(entityLoc, ItemDisplay::class.java) { display ->
            display.setItemStack(blockItem.getItem(1))
            val trans = display.transformation
            trans.scale.set(1.01f, 1.01f, 1.01f)
            display.transformation = trans
            display.isPersistent = true
            display.isInvulnerable = true
            display.persistentDataContainer.set(
                entityIdKey,
                PersistentDataType.STRING,
                blockItem.id
            )
            this.entity = display
        }
    }
}