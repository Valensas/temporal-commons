import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.spring") version "2.4.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jmailen.kotlinter") version "5.5.0"
    id("com.github.ben-manes.versions") version "0.54.0"
    id("maven-publish")
    id("java-library")
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.4"
}

group = "com.valensas"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

extra["kotlin.version"] = "2.4.0"

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:4.1.0")
    }
}

dependencies {

    //Spring Data
    api("org.springframework.data:spring-data-commons")

    // Temporal
    api("io.temporal:temporal-spring-boot-starter:1.35.0")

    //GraalVM
    implementation("com.valensas:graalvm-native-support:2.0.0")

    //Kotlin
    api("io.projectreactor.kotlin:reactor-kotlin-extensions")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    api("tools.jackson.module:jackson-module-kotlin")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_25)
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
