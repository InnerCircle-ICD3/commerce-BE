package com.fastcampus.commerce.common.error

class CoreException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
