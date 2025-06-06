plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "1.9.0"
}

group = "ru.glebik"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

    implementation("com.github.demidko:aot:2025.02.15")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}