package com.fastcampus.commerce.common.error

import com.fastcampus.commerce.common.logging.LogLevel

interface ErrorCode {
    val code: String
    val message: String
    val logLevel: LogLevel
}
