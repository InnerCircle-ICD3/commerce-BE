plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"  // 코드 스타일 검사
}

tasks.bootJar { enabled = true}
tasks.jar { enabled = false }

dependencies {
    // 다른모듈 의존성 추가
    implementation(project(":modules:product"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")
}
