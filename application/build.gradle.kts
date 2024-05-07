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
}

compose.desktop {
	application {
		mainClass = "MainKt"
	}
}

kotlin {
	jvmToolchain(17)
}

