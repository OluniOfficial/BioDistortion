package oluni.official.bioDistortion

import io.papermc.paper.plugin.loader.PluginClasspathBuilder
import io.papermc.paper.plugin.loader.PluginLoader
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.util.artifact.JavaScopes

class BioDistortionLoader : PluginLoader {
    companion object {
        private const val KOTLIN_VERSION = "2.3.0"
        private const val MCCOROUTINE_VERSION = "2.22.0"

        private val dependencies = listOf(
            "org.jetbrains.kotlin:kotlin-stdlib:$KOTLIN_VERSION",
            "com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:$MCCOROUTINE_VERSION",
            "com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:$MCCOROUTINE_VERSION"
        )
    }

    override fun classloader(classpathBuilder: PluginClasspathBuilder) {
        val resolver = MavenLibraryResolver().apply {
            addRepository(
                RemoteRepository.Builder("central", "default", MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR)
                    .build()
            )
        }
        dependencies.forEach { dependency ->
            resolver.addDependency(Dependency(DefaultArtifact(dependency), JavaScopes.COMPILE))
        }
        classpathBuilder.addLibrary(resolver)
    }
}