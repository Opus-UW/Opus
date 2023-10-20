pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
        kotlin("jvm").version(extra["kotlinVersion"] as String)
        id("org.jetbrains.compose").version(extra["composeVersion"] as String)

        kotlin("plugin.serialization").version(extra["serializationVersion"] as String)
        id("io.ktor.plugin").version(extra["ktorVersion"] as String)
    }
}

rootProject.name = "Opus"

include("application", "models", "server")
