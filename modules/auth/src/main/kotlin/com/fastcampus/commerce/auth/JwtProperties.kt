package com.fastcampus.commerce.auth

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auth.jwt")
data class JwtProperties(
    val issuer: String,
    val secret: String,
    val accessTokenExpireMinutes: Long,
    val refreshTokenExpireDays: Long,
)
