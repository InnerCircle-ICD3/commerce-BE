package com.fastcampus.commerce.common.response

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ErrorDetail? = null,
    val message: String? = null,
) {
    data class ErrorDetail(
        val code: String,
        val details: Map<String, Any>? = null,
        val message: String,
    )

    companion object {
        fun <T> success(data: T, message: String?): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
                message = message,
            )
        }

        fun <T> success(data: T): ApiResponse<T> {
            return success(data, null)
        }

        fun success(message: String): ApiResponse<Nothing?> {
            return success(data = null, message = message)
        }

        fun success(): ApiResponse<Nothing?> {
            return success(data = null)
        }

        fun failure(error: ErrorDetail, message: String?): ApiResponse<Nothing?> {
            return ApiResponse(
                success = false,
                error = error,
                message = message,
            )
        }

        fun failure(error: ErrorDetail): ApiResponse<Nothing?> {
            return failure(error, error.message)
        }
    }
}
