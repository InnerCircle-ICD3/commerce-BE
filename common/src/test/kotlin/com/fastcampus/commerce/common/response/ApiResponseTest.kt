package com.fastcampus.commerce.common.response

import com.fastcampus.commerce.common.error.CommonErrorCode
import com.fastcampus.commerce.common.error.ErrorMessage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ApiResponseTest : FunSpec(
    {
        test("성공 응답을 생성할 수 있다.") {
            val expectedData = "데이터"

            val sut = ApiResponse.success(expectedData)

            sut.data shouldBe expectedData
        }
        test("실패 응답을 생성할 수 있다.") {
            val expectedError = ErrorMessage(CommonErrorCode.SERVER_ERROR)

            val sut = ApiResponse.error(expectedError)

            sut.error shouldBe expectedError
        }
    },
)
