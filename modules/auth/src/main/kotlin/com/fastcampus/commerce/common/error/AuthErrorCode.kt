package com.fastcampus.commerce.common.error

import com.fastcampus.commerce.common.logging.LogLevel

enum class AuthErrorCode(
    override val code: String,
    override val message: String,
    override val logLevel: LogLevel,
) : ErrorCode {
    TOKEN_NOT_FOUND("AUTH-001", "토큰이 존재하지 않습니다.", LogLevel.WARN),
    EXPIRED_TOKEN("AUTH-002", "토큰이 만료되었습니다.", LogLevel.WARN),
    INVALID_TOKEN("AUTH-003", "유효하지 않은 토큰입니다.", LogLevel.WARN),
}
