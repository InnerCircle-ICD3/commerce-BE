package com.fastcampus.commerce.auth.interfaces.web.security.oauth

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

private val log = LoggerFactory.getLogger(CustomOAuth2SuccessHandler::class.java)

@Component
class CustomOAuth2SuccessHandler : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?,
    ) {
        // 예시: JWT 발급 후 쿠키/헤더에 세팅
        if (authentication == null || response == null) return
        val user = authentication.principal as? CustomUserPrincipal ?: return
        val token = "여기서 JWT 발급" //TODO: 여기서 user 객체 사용

        response.addHeader("Authorization", "Bearer $token")
        // 또는 리다이렉트
        response.sendRedirect("/")

        log.info("Successfully authenticated: $response")
    }

}
