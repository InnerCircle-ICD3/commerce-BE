dependencies {
    // Spring Security Core
    implementation("org.springframework.boot:spring-boot-starter-security")

    // OAuth2 client support (e.g., Kakao social login)
    implementation("org.springframework.security:spring-security-oauth2-client")

    // JWT support (via JJWT)
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
}
