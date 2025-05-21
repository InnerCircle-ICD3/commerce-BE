package com.fastcampus.commerce.common.error

data class ErrorMessage private constructor(
    val code: String,
    val message: String,
) {
    constructor(errorCode: ErrorCode) : this(
        code = errorCode.code,
        message = errorCode.message,
    )
}

