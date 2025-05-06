package com.fastcampus.commerce.app.response

import com.fastcampus.commerce.common.response.ApiResponse
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApiResponseAdviceTest(
    @Autowired private val mockMvc: MockMvc,
) : DescribeSpec(
        {
            describe("ApiResponseAdvice") {

                it("String을 반환하면 ApiResponse로 감싸진다") {
                    mockMvc.get("/test/string")
                        .andExpect {
                            status { isOk() }
                            jsonPath("$.success") { value(true) }
                            jsonPath("$.data") { value("hello world") }
                            jsonPath("$.error") { doesNotExist() }
                            jsonPath("$.message") { doesNotExist() }
                        }
                }

                it("객체를 반환하면 ApiResponse로 감싸진다") {
                    mockMvc.get("/test/object")
                        .andExpect {
                            status { isOk() }
                            jsonPath("$.success") { value(true) }
                            jsonPath("$.data.foo") { value("bar") }
                            jsonPath("$.error") { doesNotExist() }
                            jsonPath("$.message") { doesNotExist() }
                        }
                }

                it("ErrorDetail을 반환하면 실패 응답으로 변환된다") {
                    mockMvc.get("/test/error")
                        .andExpect {
                            status { isOk() }
                            jsonPath("$.success") { value(false) }
                            jsonPath("$.data") { doesNotExist() }
                            jsonPath("$.error.code") { value("TEST-001") }
                            jsonPath("$.error.message") { value("에러입니다") }
                            jsonPath("$.error.details.foo") { value("bar") }
                            jsonPath("$.message") { value("에러입니다") }
                        }
                }

                it("ApiResponse 객체를 반환하면 그대로 응답된다") {
                    mockMvc.get("/test/api-response")
                        .andExpect {
                            status { isOk() }
                            jsonPath("$.success") { value(true) }
                            jsonPath("$.data.foo") { value("bar") }
                            jsonPath("$.error") { doesNotExist() }
                            jsonPath("$.message") { doesNotExist() }
                        }
                }
            }
        },
    )

@RestController
@RequestMapping("/test")
class ApiResponseAdviceTestController {
    @GetMapping("/string")
    fun getString(): String {
        return "hello world"
    }

    @GetMapping("/object")
    fun getObject(): Map<String, Any> {
        return mapOf("foo" to "bar")
    }

    @GetMapping("/error")
    fun getError(): ApiResponse.ErrorDetail {
        return ApiResponse.ErrorDetail(
            code = "TEST-001",
            message = "에러입니다",
            details = mapOf("foo" to "bar"),
        )
    }

    @GetMapping("/api-response")
    fun getApiResponse(): ApiResponse<Map<String, Any>> {
        return ApiResponse.success(data = mapOf("foo" to "bar"))
    }
}
