val jjwtVersion = "0.11.5"
//const val mockitoVersion = "4.5.1"

dependencies {
    // Spring Security Core
    implementation("org.springframework.boot:spring-boot-starter-security")

    // OAuth2 client support (e.g., Kakao social login)
    implementation("org.springframework.security:spring-security-oauth2-client")

    // JWT support (via JJWT)
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")
}
