import java.util.Properties

plugins {
    kotlin("jvm") version "1.6.10" // Use the appropriate Kotlin version
    id("com.github.johnrengelman.shadow") version "7.0.0" // Shadow plugin for creating a fat JAR
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("org.jetbrains.dokka") version "1.9.10"
    `maven-publish` // Required for publishing the library
}

// Read properties file
val versionProperties =
    Properties().apply {
        load(file("version.properties").inputStream())
    }

// Set the library version
version = versionProperties["version"] as String
group = "dev.onelenyk" // Replace with your group id

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
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
        attributes["Main-Class"] = group + "LibraryKt" // Replace with your main class
    }
}

tasks.test {
    useJUnitPlatform()
}

// dokka

tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

// Configure publishing
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["dokkaHtmlJar"])
        }
    }
}
