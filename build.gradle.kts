plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.shadow)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.bundles.mccoroutine)
}

tasks {
    runServer {
        minecraftVersion("1.21.8")
    }
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        relocate("kotlin", "oluni.official.libs.kotlin")
        relocate("kotlinx", "oluni.official.libs.kotlinx")
        relocate("com.github.shynixn.mccoroutine", "oluni.official.libs.mccoroutine")
    }
}

kotlin {
    jvmToolchain(21)
}

