plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
}

kotlin {
    jvmToolchain(21)
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}