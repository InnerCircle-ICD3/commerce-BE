val javaVersion: String by project
val springBootVersion: String by project
val kotestVersion: String by project
val springKotestVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring") apply false
    kotlin("plugin.jpa") apply false
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
    id("org.jlleitschuh.gradle.ktlint") apply false
}

allprojects {
    group = project.property("projectGroup") as String
    version = project.property("applicationVersion") as String

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    java.sourceCompatibility = JavaVersion.toVersion(javaVersion)

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        testImplementation("io.kotest:kotest-runner-junit5:${kotestVersion}")
        testImplementation("io.kotest:kotest-assertions-core:${kotestVersion}")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:${springKotestVersion}")

        if (project.name != "common") {
            implementation("org.springframework.boot:spring-boot-starter-web")
            implementation("org.springframework.boot:spring-boot-starter-data-jpa")

            runtimeOnly("com.h2database:h2")
            runtimeOnly("org.postgresql:postgresql")
        }

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        kapt("org.springframework.boot:spring-boot-configuration-processor")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
        }
    }

    tasks.named<Jar>("bootJar") {
        enabled = false
    }

    tasks.named<Jar>("jar") {
        enabled = true
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = javaVersion
        }
    }

    tasks.test {
        useJUnitPlatform()
        systemProperty("user.timezone", "UTC")
    }
}
