val springMockkVersion: String by project
val querydslVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.jlleitschuh.gradle.ktlint")
}

dependencies {
    implementation(project(":common"))

    implementation("io.github.openfeign.querydsl:querydsl-core:$querydslVersion")
    implementation("io.github.openfeign.querydsl:querydsl-jpa:$querydslVersion")
    kapt("io.github.openfeign.querydsl:querydsl-apt:$querydslVersion:jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
}

kapt {
    correctErrorTypes = true
}

val querydslDir = "build/generated"

sourceSets["main"].java.srcDirs(querydslDir)
