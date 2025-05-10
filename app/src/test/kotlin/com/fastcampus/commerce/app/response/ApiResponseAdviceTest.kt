package com.fastcampus.commerce.app.response

import com.fastcampus.commerce.common.response.ApiResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.servlet.ServletException

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApiResponseAdviceTest(
    @Autowired private val mockMvc: MockMvc,
) : DescribeSpec(
        {
            describe("ApiResponseAdvice") {
                it("String을 반환하면 IllegalStateException를 반환한다.") {
                    val thrown = shouldThrow<ServletException> {
                        mockMvc.get("/test/string")
                    }

                    thrown.rootCause is IllegalStateException
                    thrown.rootCause.message shouldBe "String 리턴 금지"
                }

                it("객체를 반환하면 ApiResponse로 감싸진다") {
                    mockMvc.get("/test/object")
                        .andExpect {
                            status { isOk() }
                            jsonPath("$.data.foo") { value("bar") }
                            jsonPath("$.error") { doesNotExist() }
                        }
                }

                it("ApiResponse 객체를 반환하면 그대로 응답된다") {
                    mockMvc.get("/test/api-response")
                        .andExpect {
                            status { isOk() }
                            jsonPath("$.data.foo") { value("bar") }
                            jsonPath("$.error") { doesNotExist() }
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

    @GetMapping("/api-response")
    fun getApiResponse(): ApiResponse<Map<String, Any>> {
        return ApiResponse.success(data = mapOf("foo" to "bar"))
    }
}
