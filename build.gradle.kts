import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val javaVersion: String by project
val springBootVersion: String by project
val springMockkVersion: String by project
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

java.sourceCompatibility = JavaVersion.toVersion(javaVersion)

allprojects {
    group = "${property("projectGroup")}"
    version = "${property("applicationVersion")}"

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

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        testImplementation(kotlin("test"))
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:$springKotestVersion")

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        kapt("org.springframework.boot:spring-boot-configuration-processor")

        // 프로젝트가 커진다면 별도의 Convention Plugin 사용을 고려할 수 있음
        if (project.name != "common") {
            runtimeOnly("org.postgresql:postgresql")
        }
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
        }
    }

    tasks.getByName("bootJar") {
        enabled = false
    }

    tasks.getByName("jar") {
        enabled = true
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = javaVersion
        }
    }

    tasks.test {
        useJUnitPlatform()
        systemProperty("user.timezone", "UTC")
    }
}

fun String.runCommand(): String =
    ProcessBuilder(*split(" ").toTypedArray())
        .redirectErrorStream(true)
        .start()
        .inputStream.bufferedReader().readText().trim()

tasks.register("printChangedModules") {
    group = "ci"
    doLast {
        val baseCommit = project.findProperty("baseCommit") as? String ?: "origin/master"
        val changedFiles = "git diff --name-only $baseCommit".runCommand().lines()

        val changedModules = subprojects.mapNotNull { sub ->
            val path = sub.projectDir.relativeTo(rootDir).path
            if (changedFiles.any { it.startsWith("$path/") }) sub.name else null
        }

        println(changedModules.joinToString(" "))
    }
}

