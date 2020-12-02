plugins {
    java
    kotlin("jvm") version "1.4.20"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "com.github.imalwayscoding"
version = "1.0"

repositories {
    mavenCentral()

    // paper
    maven("https://papermc.io/repo/repository/maven-public/")

    // jitpack
    maven("https://jitpack.io/")
}

dependencies {
    implementation(kotlin("stdlib"))

    // paper
    compileOnly("com.destroystokyo.paper:paper-api:1.16.4-R0.1-SNAPSHOT")

    // kommand
    implementation("com.github.noonmaru:kommand:0.6.3")
}

tasks {
    shadowJar {
        archiveName = "Party.jar"
    }
}
