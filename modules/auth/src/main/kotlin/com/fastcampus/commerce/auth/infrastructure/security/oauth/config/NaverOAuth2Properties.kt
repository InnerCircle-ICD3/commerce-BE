package com.fastcampus.commerce.auth.infrastructure.security.oauth.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import jakarta.annotation.PostConstruct

private val log = LoggerFactory.getLogger(NaverOAuth2Properties::class.java)

@ConfigurationProperties(prefix = "auth.oauth2.naver")
data class NaverOAuth2Properties(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
    val scope: List<String>,
    val authorizationGrantType: String,
) {
    @PostConstruct
    fun logProps() {
        println("Naver OAuth2 Config:")
        println("- clientId: $clientId")
        println("- clientSecret: $clientSecret")
        println("- redirectUri: $redirectUri")
        println("- authorizationGrantType: $authorizationGrantType")
        println("- scope: $scope")
        log.info("Naver OAuth2 NaverOAuth2Properties")
        log.info("- clientId: $clientId")
        log.info("- clientSecret: $clientSecret")
        log.info("- redirectUri: $redirectUri")
        log.info("- authorizationGrantType: $authorizationGrantType")
        log.info("- scope: $scope")
    }
}
