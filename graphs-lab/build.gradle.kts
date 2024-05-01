plugins {
    kotlin("jvm") version "1.9.20"
    `java-library`
    jacoco
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // https://mvnrepository.com/artifact/org.jgrapht/jgrapht-core
    implementation("org.jgrapht:jgrapht-core:1.5.2")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.3")
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
}

detekt {
    config.setFrom("../scripts/detekt-config.yml")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        showCauses = false
        showStackTraces = false
        showExceptions = false
    }
}

tasks.named<JacocoReport>("jacocoTestReport") {
    val sep = File.separator
    val jacocoReportsDirName = "reports${sep}jacoco"
    reports {
        csv.required = true
        xml.required = false
        html.required = true
        csv.outputLocation = layout.buildDirectory.file("${jacocoReportsDirName}${sep}info.csv")
        html.outputLocation = layout.buildDirectory.dir("${jacocoReportsDirName}${sep}html")
    }
}
