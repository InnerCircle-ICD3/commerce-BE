package com.fastcampus.commerce.auth.interfaces.web.security.oauth

import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

private val log = LoggerFactory.getLogger(CustomOAuth2FailureHandler::class.java)

@Component
class CustomOAuth2FailureHandler : AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?,
    ) {
        // 1. 실패 로그 기록 (원인 파악에 필수)
        println("OAuth2 로그인 실패: ${exception?.message}")
        log.info("OAuth2 로그인 실패 원인: ${exception?.cause}")

        // 2. 실패 시 로그인 페이지로 리다이렉트 + 에러 파라미터 전달
        response?.sendRedirect("/auth/login?error=" + (exception?.message ?: "oauth2_failure"))
    }
}
