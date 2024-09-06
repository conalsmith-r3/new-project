plugins {
    `kotlin-dsl`
    `maven-publish`
    id("com.jfrog.artifactory") version "5.2.5"
    id("com.r3.gradle.standards.kotlin-common")
//    id("com.r3.gradle.standards.publication.maven-publication") version "0.2.15-bm_r3sol-649_updating-a9y-defaults.96896e3-SNAPSHOT"
//    id("com.r3.gradle.standards.publication.terraform-publication") version "0.3.0-r3sol-364.a2028d6-SNAPSHOT"
}

group = "com.r3.example"
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

group = "com.r3.example"
version = "0.0.3-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

dependencies {
    testImplementation(libs.bundles.test)
}

//terraform {
//    modules = mapOf(
//        "terraform-zip" to "$projectDir/terraform"
//    )
//    provider = "kubernetes"
//    repositoryKey = "terraform-modules-dev"
//    tag = "conal-123"
//    namespace = "abc"
//}


artifactory {
    publish {
        val props = (extra.properties + System.getenv()).mapKeys { it.key.lowercase().replace("_", "").replace("corda","") }
        contextUrl = props["artifactoryUrl".lowercase()]?.toString() ?: ""

        repository {
            username = props["artifactoryUsername".lowercase()]?.toString() ?: ""
            password = props["artifactoryPassword".lowercase()]?.toString() ?: ""
            repoKey = props["artifactoryRepositoryKey".lowercase()]?.toString() ?: ""
        }

        defaults {
            setPublishPom(true)
            setPublishArtifacts(true)
            isPublishBuildInfo = true
            publications("ALL_PUBLICATIONS")
        }
    }
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin")
        resources.srcDirs("src/main/resources")
    }
}

java {
    withSourcesJar()
}

// TODO: remove once R3SOL-298 is implemented.
tasks.named("publish") {
    finalizedBy("artifactoryPublish")
}
