val querydslVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

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
