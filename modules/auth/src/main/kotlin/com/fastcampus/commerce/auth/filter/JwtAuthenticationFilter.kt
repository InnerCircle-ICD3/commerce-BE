package com.fastcampus.commerce.auth.filter

import com.fastcampus.commerce.auth.HttpHeaderKeys
import com.fastcampus.commerce.auth.TokenProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.ObjectUtils
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val accessToken = request.getHeader(HttpHeaderKeys.ACCESS_TOKEN)

        // TODO 에러 메시지 변경
        if (ObjectUtils.isEmpty(accessToken)) {
            throw IllegalArgumentException("토큰이 존재하지 않습니다.")
        }

        try {

            if (accessToken != null && tokenProvider.validateToken(accessToken)) {
                val authentication = tokenProvider.getAuthentication(accessToken)
                SecurityContextHolder.getContext().authentication = authentication
                logger.debug("JWT 인증 성공: $accessToken")
            }
        } catch (e: Exception) {
            logger.error("JWT 인증 실패: ${e.message}")
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)


    }

}
