val springMockkVersion: String by project

dependencies {
    implementation(project(":common"))
    implementation(project(":modules:admin"))
    implementation(project(":modules:auth"))
    implementation(project(":modules:cart"))
    implementation(project(":modules:chat"))
    implementation(project(":modules:order"))
    implementation(project(":modules:payment"))
    implementation(project(":modules:product"))
    implementation(project(":modules:review"))
    implementation(project(":modules:user"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
}
