package com.fastcampus.commerce.response

import com.fastcampus.commerce.common.response.ApiResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class ApiResponseAdvice(
    private val objectMapper: ObjectMapper,
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
            return objectMapper.writeValueAsString(ApiResponse.success(data = body))
        }
        if (body is ApiResponse<*>) {
            return body
        }

        if (body is ApiResponse.ErrorDetail) {
            return ApiResponse.failure(error = body)
        }

        return ApiResponse.success(data = body)
    }
}
