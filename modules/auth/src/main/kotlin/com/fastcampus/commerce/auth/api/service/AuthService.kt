package com.fastcampus.commerce.auth.api.service

import com.fastcampus.commerce.auth.TokenProvider
import com.fastcampus.commerce.common.error.AuthErrorCode
import com.fastcampus.commerce.common.error.CoreException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val tokenProvider: TokenProvider
) {

    /**
     * 액세스 토큰을 재발급합니다.
     *
     * @param refreshToken 리프레시 토큰
     * @return 새로 발급된 액세스 토큰
     * @throws CoreException 토큰이 유효하지 않거나 만료된 경우
     */
    fun reissueToken(refreshToken: String): String {
        try {
            // 리프레시 토큰 검증 및 새 액세스 토큰 발급
            return tokenProvider.refreshAccessToken(refreshToken)
        } catch (e: Exception) {
            throw CoreException(AuthErrorCode.INVALID_TOKEN)
        }
    }
}
