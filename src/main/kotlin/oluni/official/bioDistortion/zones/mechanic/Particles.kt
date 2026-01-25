package oluni.official.bioDistortion.zones.mechanic

import oluni.official.bioDistortion.BioDistortion
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable

class Particles(val plugin: BioDistortion) {

    fun greenParticlesRunnable(location: Location) {
        object : BukkitRunnable() {
            private var animationTimeInTicks = 0
            override fun run() {
                if (animationTimeInTicks >= 200) {
                    cancel()
                    return
                }
                val world = location.world ?: return
                for (i in 0..20) {
                    val relativeY = i * 0.2
                    val yPosition = location.y + relativeY + (animationTimeInTicks * 0.1)
                    world.spawnParticle(
                        Particle.POOF,
                        location.x + 0.5, yPosition, location.z + 0.5,
                        1,
                        0.0, 0.0, 0.0, 0.0
                    )
                }
                animationTimeInTicks++
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }
}