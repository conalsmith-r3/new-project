pluginManagement {
    // Manually parse the Toml file to workaround issues with version catalog not supporting direct use in settings.gradle
    val catalog = file("gradle/libs.versions.toml").readText()

    fun extractVersion(id: String): String {
        val matcher = java.util.regex.Pattern.compile("${id}\\s*=\\s*\"([^\"]+)\"").matcher(catalog)
        return if (matcher.find()) {
            matcher.group(1)
        } else {
            throw IllegalArgumentException("No version found for dependency with $id.")
        }
    }

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

    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version extractVersion("foojay-resolver-convention-plugin")
        id("com.r3.gradle.standards.kotlin-common") version extractVersion("kotlin-common-plugin")
        id("com.r3.gradle.standards.publication.maven-publication") version extractVersion("maven-publication-plugin")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention")
}

val resolvedProperties = (extra.properties + System.getenv()).mapKeys { it.key.lowercase().replace("_", "").replace("corda","") }

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenLocal()

        maven {
            url = uri("${resolvedProperties["artifactoryUrl".lowercase()] ?: ""}/corda-ent-maven")
            credentials {
                username = resolvedProperties["artifactoryUsername".lowercase()]?.toString() ?: ""
                password = resolvedProperties["artifactoryPassword".lowercase()]?.toString() ?: ""
            }
        }

        mavenCentral()
    }
}

rootProject.name = "new-project"

