plugins {
    kotlin("jvm") version "1.9.20"
    `java-library`
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}