val jjwtVersion = "0.11.5"

dependencies {

    // WebClient
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // Spring Security Core
    implementation("org.springframework.boot:spring-boot-starter-security")

    // OAuth2 client support (e.g., Kakao social login)
    implementation("org.springframework.security:spring-security-oauth2-client")

    // JWT support (via JJWT)
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")
}
