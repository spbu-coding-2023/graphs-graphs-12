plugins {
	kotlin("jvm") version "1.9.20"
	id("org.jetbrains.compose") version "1.5.10"
}

repositories {
	mavenCentral()
	maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	google()
}

val sqliteJdbcVersion: String by project
dependencies {
	implementation(compose.desktop.currentOs)
	implementation(compose.materialIconsExtended)
	implementation(compose.material3)
	implementation(compose.foundation)
	implementation(project(":graphs-lab"))
	implementation("org.neo4j.driver", "neo4j-java-driver", "4.4.5")
	implementation("org.json:json:20240303")
	implementation("org.xerial", "sqlite-jdbc", sqliteJdbcVersion)
	implementation("io.github.microutils", "kotlin-logging-jvm", "2.0.6")
	implementation("org.slf4j", "slf4j-simple", "1.7.29")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1")
}

compose.desktop {
	application {
		mainClass = "MainKt"
	}
}

kotlin {
	jvmToolchain(17)
}
