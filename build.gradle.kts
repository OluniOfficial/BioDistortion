plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.run.paper)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.bundles.mccoroutine)
}

tasks {
    runServer {
        minecraftVersion("1.21.8")
    }
    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}

kotlin {
    jvmToolchain(21)
}
