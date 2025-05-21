package com.fastcampus.commerce.auth.filter

import com.fastcampus.commerce.auth.HttpHeaderKeys
import com.fastcampus.commerce.auth.TokenProvider
import com.fastcampus.commerce.common.error.AuthErrorCode
import com.fastcampus.commerce.common.error.CoreException
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val accessToken = request.getHeader(HttpHeaderKeys.ACCESS_TOKEN)
            ?: throw CoreException(AuthErrorCode.TOKEN_NOT_FOUND)

        val userId = tokenProvider.extractUserIdFromToken(accessToken)

        response.setHeader(HttpHeaderKeys.USER_ID, userId.toString())

        filterChain.doFilter(request, response)
    }

}
