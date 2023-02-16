val tgbotapiVersion: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    application
}

group = "com.khannan"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0-RC")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("dev.inmo:tgbotapi:$tgbotapiVersion")
    implementation("dev.inmo:tgbotapi.core:$tgbotapiVersion")
    implementation("dev.inmo:tgbotapi.api:$tgbotapiVersion")
    implementation("dev.inmo:tgbotapi.utils:$tgbotapiVersion")
    implementation("ch.qos.logback:logback-classic:1.3.5")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}