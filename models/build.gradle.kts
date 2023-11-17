plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "org.team202.models"
version = "1.0-SNAPSHOT"
val exposedVersion: String by project
val sqliteVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")
    implementation("org.xerial:sqlite-jdbc:$sqliteVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
