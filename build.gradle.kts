plugins {
    kotlin("jvm") version "1.6.10" // Use the appropriate Kotlin version
    id("com.github.johnrengelman.shadow") version "7.0.0" // Shadow plugin for creating a fat JAR
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    `maven-publish` // Required for publishing the library
}

group = "dev.onelenyk" // Replace with your group id
version = "0.1.0" // Your library version

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

// Configure publishing
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

// Include necessary information for JitPack
tasks.named<Jar>("jar") {
    manifest {
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] = project.version
    }
}

// Shadow plugin configuration to create a fat JAR (if needed)
tasks.shadowJar {
    archiveClassifier.set("")
    manifest {
        attributes["Main-Class"] = group + "MainKt" // Replace with your main class
    }
}

tasks.test {
    useJUnitPlatform()
}
