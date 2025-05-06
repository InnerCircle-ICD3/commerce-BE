package com.fastcampus.commerce.common.response

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class ApiResponseTest : FunSpec(
    {

        context("ApiResponse 기본 생성자") {
            test("모든 필드를 수동으로 설정해서 생성할 수 있다") {
                val expectedSuccess = true
                val expectedData = "데이터"
                val expectedError = ApiResponse.ErrorDetail(
                    code = "E001",
                    message = "에러 발생",
                    details = mapOf("foo" to "bar"),
                )
                val expectedMessage = "요청 성공"

                val actual = ApiResponse(
                    success = expectedSuccess,
                    data = expectedData,
                    error = expectedError,
                    message = expectedMessage,
                )

                actual.success shouldBe expectedSuccess
                actual.data shouldBe expectedData
                actual.error shouldBe expectedError
                actual.message shouldBe expectedMessage
            }
        }

        context("성공 응답 생성") {

            test("data와 message를 함께 포함한 응답 생성") {
                val expectedData = "데이터"
                val expectedMessage = "처리 완료"

                val actual = ApiResponse.success(expectedData, expectedMessage)

                actual.success.shouldBeTrue()
                actual.data shouldBe expectedData
                actual.error.shouldBeNull()
                actual.message shouldBe expectedMessage
            }

            test("data만 포함한 응답 생성") {
                val expectedData = "데이터"

                val actual = ApiResponse.success<String>(expectedData)

                actual.success.shouldBeTrue()
                actual.data shouldBe expectedData
                actual.error.shouldBeNull()
                actual.message.shouldBeNull()
            }

            test("message만 포함한 응답 생성") {
                val expectedMessage = "처리 완료"

                val actual = ApiResponse.success(message = expectedMessage)

                actual.success.shouldBeTrue()
                actual.data.shouldBeNull()
                actual.error.shouldBeNull()
                actual.message shouldBe expectedMessage
            }

            test("data, message 없이 빈 성공 응답 생성") {
                val actual = ApiResponse.success()

                actual.success.shouldBeTrue()
                actual.data.shouldBeNull()
                actual.error.shouldBeNull()
                actual.message.shouldBeNull()
            }
        }

        context("실패 응답 생성") {

            test("에러 객체와 메시지를 포함한 실패 응답 생성") {
                val expectedError = ApiResponse.ErrorDetail(
                    code = "E001",
                    message = "필드 오류",
                    details = mapOf("foo" to "bar"),
                )
                val expectedMessage = "입력값이 올바르지 않습니다"

                val actual = ApiResponse.failure(expectedError, expectedMessage)

                actual.success.shouldBeFalse()
                actual.data.shouldBeNull()
                actual.error shouldBe expectedError
                actual.message shouldBe expectedMessage
            }

            test("에러 객체만 포함한 실패 응답 생성") {
                val expectedError = ApiResponse.ErrorDetail(
                    code = "E002",
                    message = "권한 없음",
                    details = mapOf("foo" to "bar"),
                )

                val actual = ApiResponse.failure(expectedError)

                actual.success.shouldBeFalse()
                actual.data.shouldBeNull()
                actual.error shouldBe expectedError
                actual.message shouldBe expectedError.message
            }
        }

        context("ErrorDetail 생성") {

            test("details를 포함한 ErrorDetail 생성") {
                val code = "E003"
                val message = "세부 에러"
                val details = mapOf("foo" to "bar")

                val errorDetail = ApiResponse.ErrorDetail(
                    code = code,
                    message = message,
                    details = details,
                )

                errorDetail.code shouldBe code
                errorDetail.message shouldBe message
                errorDetail.details shouldBe details
            }

            test("details 없이 ErrorDetail 생성") {
                val code = "E004"
                val message = "단순 에러"

                val errorDetail = ApiResponse.ErrorDetail(
                    code = code,
                    message = message,
                )

                errorDetail.code shouldBe code
                errorDetail.message shouldBe message
                errorDetail.details.shouldBeNull()
            }
        }
    },
)
