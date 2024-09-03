plugins {
    `kotlin-dsl`
    id("com.r3.gradle.standards.kotlin-common")
    id("com.r3.gradle.standards.publication.maven-publication")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    gradlePluginPortal()

    maven {
        val props = (extra.properties + System.getenv()).mapKeys { it.key.lowercase().replace("_", "").replace("corda","") }
        url = uri("${props["artifactoryUrl".lowercase()] ?: ""}/engineering-tools-maven")
        credentials {
            username = props["artifactoryUsername".lowercase()]?.toString() ?: ""
            password = props["artifactoryPassword".lowercase()]?.toString() ?: ""
        }
    }

    mavenCentral()
}

group = "com.r3.corda.template"
version = "0.0.1-SNAPSHOT"
