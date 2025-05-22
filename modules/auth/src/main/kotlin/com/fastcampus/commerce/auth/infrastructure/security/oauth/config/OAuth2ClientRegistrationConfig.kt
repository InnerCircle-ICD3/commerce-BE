package com.fastcampus.commerce.auth.infrastructure.security.oauth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType

@Configuration
class OAuth2ClientRegistrationConfig(
    private val naverProps: NaverOAuth2Properties
) {

    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        val naver = ClientRegistration.withRegistrationId("naver")
            .clientId(naverProps.clientId)
            .clientSecret(naverProps.clientSecret)
            .redirectUri(naverProps.redirectUri)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope(*naverProps.scope.toTypedArray())
            .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
            .tokenUri("https://nid.naver.com/oauth2.0/token")
            .userInfoUri("https://openapi.naver.com/v1/nid/me")
            .userNameAttributeName("id")
            .clientName("Naver")
            .build()

        return InMemoryClientRegistrationRepository(naver)
    }
}
