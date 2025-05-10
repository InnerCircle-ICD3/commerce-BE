rootProject.name = "commerce"

include(
    ":app",
    ":common",
    ":infrastructure",
    ":modules:admin",
    ":modules:auth",
    ":modules:cart",
    ":modules:chat",
    ":modules:member",
    ":modules:payment",
    ":modules:product",
    ":modules:review",
    ":modules:order",
)

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val ktlintVersion: String by settings
    val kotlinSdkVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("kapt") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
