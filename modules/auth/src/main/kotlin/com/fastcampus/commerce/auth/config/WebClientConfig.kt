package com.fastcampus.commerce.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun userWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://api.801base.com/userModule") // TODO: User Module 임시 설정, 추후 변경 필요
            .build()
    }
}
