package com.fastcampus.commerce.auth.config

import com.fastcampus.commerce.auth.infrastructure.security.oauth.config.NaverOAuth2Properties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@ConfigurationProperties(prefix = "auth.cors")
class CorsProperties {
    var allowedOrigins: List<String> = listOf("http://localhost:8080")
    var allowedMethods: List<String> = listOf("GET", "POST", "PUT", "DELETE")
    var allowedHeaders: List<String> = listOf("Authorization", "Content-Type")
}

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(NaverOAuth2Properties::class)
class SecurityConfig(
    private val corsProperties: CorsProperties,
    private val customOAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
    private val customSuccessHandler: AuthenticationSuccessHandler,
    private val customFailureHandler: AuthenticationFailureHandler
) {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        //cors 모두 허용
        val config = CorsConfiguration().apply {
            allowedOrigins = corsProperties.allowedOrigins
            allowedMethods = corsProperties.allowedMethods
            allowedHeaders = corsProperties.allowedHeaders
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, corsConfigurationSource: CorsConfigurationSource): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource) }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/auth/login", "/auth/signup", "/oauth2/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .loginPage("/auth/login") // 커스텀 로그인 페이지가 있다면 설정
                    .authorizationEndpoint { auth ->
                        auth.baseUri("/oauth2/authorization") // ex) /oauth2/authorization/naver
                    }
                    .redirectionEndpoint { redir ->
                        redir.baseUri("/login/oauth2/code/*") // redirect-uri 와 일치
                    }
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(customOAuth2UserService) // OAuth2UserService 구현체 주입 필요
                    }
                    .successHandler(customSuccessHandler) // 선택
                    .failureHandler(customFailureHandler) // 선택
            }
        return http.build()
    }
}
