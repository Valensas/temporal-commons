import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.1.10"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jmailen.kotlinter") version "5.0.1"
    id("com.github.ben-manes.versions") version "0.52.0"
    id("maven-publish")
    id("java-library")
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.4"
}

group = "com.valensas"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.2")
    }
}

dependencies {

    //Spring Data
    api("org.springframework.data:spring-data-commons")

    // Temporal
    api("io.temporal:temporal-spring-boot-starter:1.32.1")

    //GraalVM
    implementation("com.valensas:graalvm-native-support:1.0.9")

    //Kotlin
    api("io.projectreactor.kotlin:reactor-kotlin-extensions")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

publishing {
    publications {
        create("library", MavenPublication::class.java) {
            artifactId = "temporal-commons"
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

signing {
    val keyId = System.getenv("SIGNING_KEYID")
    val secretKey = System.getenv("SIGNING_SECRETKEY")
    val passphrase = System.getenv("SIGNING_PASSPHRASE")

    useInMemoryPgpKeys(keyId, secretKey, passphrase)
}

centralPortal {
    name = "temporal-commons"
    username = System.getenv("SONATYPE_USERNAME")
    password = System.getenv("SONATYPE_PASSWORD")
    pom {
        name = "Temporal Commons"
        description = "Common utilities for Temporal workflow integration with Spring Boot."
        url = "https://valensas.com/"
        scm {
            url = "https://github.com/Valensas/temporal-commons"
        }

        licenses {
            license {
                name.set("MIT License")
                url.set("https://mit-license.org")
            }
        }

        developers {
            developer {
                id.set("0")
                name.set("Valensas")
                email.set("info@valensas.com")
            }
        }
    }
}
