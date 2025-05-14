val jjwtVersion: String by project

dependencies {
    implementation(project(":modules:user"))

    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Oauth Client
    implementation("org.springframework.security:spring-security-oauth2-client")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
}
