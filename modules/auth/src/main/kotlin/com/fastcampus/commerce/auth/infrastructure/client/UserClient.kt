package com.fastcampus.commerce.auth.infrastructure.client

import com.fastcampus.commerce.auth.interfaces.web.security.oauth.NaverUserResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

data class UserDto(
    val id: Long,
    val email: String?,
    val nickname: String,
    val roles: List<String>
)

@Component
class UserApiClient(
    @Qualifier("userWebClient") private val webClient: WebClient
) {
    private val log = LoggerFactory.getLogger(UserApiClient::class.java)
    fun saveOrUpdateUser(oauthInfo: NaverUserResponse): UserDto {
        try {
            return webClient.post()
                .uri("/api/users/oauth2")
                .bodyValue(oauthInfo)
                .retrieve()
                .bodyToMono(UserDto::class.java)
                .block() ?: throw IllegalStateException("User API 응답이 null입니다.")
        } catch (ex: Exception) {
            log.error("User API 호출 실패: ${ex.message}", ex)
            throw IllegalStateException("User API 호출 중 에러", ex)
        }
    }
}
