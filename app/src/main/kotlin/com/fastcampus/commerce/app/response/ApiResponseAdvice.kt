package com.fastcampus.commerce.app.response

import com.fastcampus.commerce.common.response.ApiResponse
import org.springframework.core.MethodParameter
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class ApiResponseAdvice(
    private val environment: Environment,
) : ResponseBodyAdvice<Any> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>?>): Boolean = true

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>?>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        if (body is String) {
            val activeProfiles = environment.activeProfiles
            val isDevelopmentProfile = activeProfiles.any {
                it.equals("dev", ignoreCase = true) ||
                    it.equals("test", ignoreCase = true) ||
                    it.equals("local", ignoreCase = true)
            }
            if (isDevelopmentProfile) {
                throw IllegalStateException("String 리턴 금지")
            }
        }

        if (body is ApiResponse<*>) {
            return body
        }
        return ApiResponse.success(body)
    }
}
