package com.fastcampus.commerce.auth.interfaces.web.security.oauth

import com.fastcampus.commerce.auth.HttpHeaderKeys
import com.fastcampus.commerce.auth.TokenProvider
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

private val log = LoggerFactory.getLogger(CustomOAuth2SuccessHandler::class.java)

@Component
class CustomOAuth2SuccessHandler(
    private val tokenProvider: TokenProvider
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?,
    ) {
        if (authentication == null || response == null) return
        val user = authentication.principal as? CustomUserPrincipal ?: return

        // JWT 토큰 발급
        val userId = user.getName().toLong()
        val accessToken = tokenProvider.createAccessToken(userId)
        val refreshToken = tokenProvider.createRefreshToken(userId)

        // 헤더에 토큰 추가
        response.addHeader(HttpHeaderKeys.ACCESS_TOKEN, accessToken)
        response.addHeader(HttpHeaderKeys.USER_ID, userId.toString())

        // 쿠키에 리프레시 토큰 추가 (보안을 위해 httpOnly, secure 설정)
        val cookie = jakarta.servlet.http.Cookie("refresh_token", refreshToken).apply {
            isHttpOnly = true
            secure = true
            path = "/"
            maxAge = 60 * 60 * 24 * 14 // 14일
        }
        response.addCookie(cookie)

        log.info("Successfully authenticated user: ${user.getName()}")
    }
}
