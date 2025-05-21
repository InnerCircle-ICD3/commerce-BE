package com.fastcampus.commerce.common.response

import com.fastcampus.commerce.common.error.ErrorMessage

data class ApiResponse<T> private constructor(
    val data: T? = null,
    val error: ErrorMessage? = null,
) {
    companion object {
        fun <S> success(data: S): ApiResponse<S> {
            return ApiResponse(data, null)
        }

        fun error(errorMessage: ErrorMessage): ApiResponse<Nothing?> {
            return ApiResponse(null, errorMessage)
        }
    }
}

