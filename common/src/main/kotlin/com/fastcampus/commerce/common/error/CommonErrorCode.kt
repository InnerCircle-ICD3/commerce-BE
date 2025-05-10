package com.fastcampus.commerce.common.error

import com.fastcampus.commerce.common.logging.LogLevel

enum class CommonErrorCode(
    override val code: String,
    override val message: String,
    override val logLevel: LogLevel,
) : ErrorCode {
    SERVER_ERROR("SYS-001", "An unexpected error has occurred.", LogLevel.ERROR),
}
