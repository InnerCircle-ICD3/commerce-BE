val querydslVersion: String by project

dependencies {
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("io.github.openfeign.querydsl:querydsl-jpa:${querydslVersion}")
    kapt("io.github.openfeign.querydsl:querydsl-apt:${querydslVersion}:jpa")
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/kapt/main")
        }
    }
}
