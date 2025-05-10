package com.fastcampus.commerce.app.error

import com.fastcampus.commerce.common.error.CommonErrorCode
import com.fastcampus.commerce.common.error.CoreException
import com.fastcampus.commerce.common.error.ErrorMessage
import com.fastcampus.commerce.common.logging.LogLevel
import com.fastcampus.commerce.common.response.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    private val log: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java),
) {
    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ApiResponse<Nothing?> {
        when (e.errorCode.logLevel) {
            LogLevel.ERROR -> log.error("CoreException: {}", e.message, e)
            LogLevel.WARN -> log.warn("CoreException: {}", e.message, e)
            LogLevel.INFO -> log.info("CoreException: {}", e.message, e)
        }
        return ApiResponse.error(ErrorMessage(e.errorCode))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ApiResponse<Nothing?> {
        log.error("Exception: {}", e.message, e)
        return ApiResponse.error(ErrorMessage(CommonErrorCode.SERVER_ERROR))
    }
}
