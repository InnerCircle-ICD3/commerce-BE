plugins {
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":modules:cart"))
    implementation(project(":modules:member"))
    implementation(project(":modules:order"))
    implementation(project(":modules:payment"))
    implementation(project(":modules:product"))
    implementation(project(":modules:review"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    testRuntimeOnly("com.h2database:h2")
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
