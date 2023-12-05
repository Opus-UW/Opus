import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val slf4jVersion: String by project
val precomposeVersion: String by project

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

group = "com.team202.opus"
version = "1.1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

kotlin {

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation("com.adonax:audiocue:2.1.0")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
            implementation(compose.materialIconsExtended)
            implementation(compose.material3)
            implementation(compose.material)
            implementation(project(mapOf("path" to ":models")))

            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-cio:$ktorVersion")
            implementation("io.ktor:ktor-client-auth:$ktorVersion")
            implementation("org.slf4j:slf4j-simple:$slf4jVersion")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            implementation("io.ktor:ktor-client-logging:$ktorVersion")

            implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-beta03")

            implementation("moe.tlaster:precompose:$precomposeVersion")
            implementation("moe.tlaster:precompose-viewmodel:$precomposeVersion")

            implementation ("com.google.api-client:google-api-client:2.0.0")
            implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
            implementation ("com.google.apis:google-api-services-calendar:v3-rev20220715-2.0.0")
            implementation ("com.google.apis:google-api-services-tasks:v1-rev20210709-2.0.0")

            implementation("io.ktor:ktor-client-websockets:$ktorVersion")
            implementation ("com.google.auth:google-auth-library-oauth2-http:1.19.0")
            implementation ("com.google.auth:google-auth-library-oauth2-http:1.19.0")
            implementation("com.google.apis:google-api-services-oauth2:v2-rev20200213-2.0.0")
            implementation("media.kamel:kamel-image:0.9.0")
            implementation("com.google.apis:google-api-services-gmail:v1-rev20220404-2.0.0")
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            includeAllModules = true
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Opus"
            packageVersion = "1.1.0"
            windows {
                iconFile.set(project.file("logo.ico"))
                shortcut = true
                menu = true
            }
            macOS {
                iconFile.set(project.file("logo.icns"))
            }
            linux {
                iconFile.set(project.file("logo.png"))
            }
        }
    }
}
