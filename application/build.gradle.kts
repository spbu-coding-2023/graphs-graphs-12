plugins {
	kotlin("jvm") version "1.9.20"
	id("org.jetbrains.compose") version "1.5.10"
}

repositories {
	mavenCentral()
	maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	google()
}

dependencies {
	implementation(compose.desktop.currentOs)
	implementation(compose.materialIconsExtended)
	implementation(compose.material3)
	implementation(compose.foundation)
	implementation(project(":graphs-lab"))
	implementation("org.neo4j.driver", "neo4j-java-driver", "4.4.5")
	// https://mvnrepository.com/artifact/org.json/json
	implementation("org.json:json:20240303")
	implementation("com.darkrockstudios:mpfilepicker:3.1.0")
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
