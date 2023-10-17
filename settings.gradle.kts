pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("jvm").version(extra["kotlin.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)

        kotlin("plugin.serialization").version(extra["serialization.version"] as String)
        id("io.ktor.plugin").version(extra["ktor.version"] as String)
    }
}

rootProject.name = "Opus"

include("application", "models")
