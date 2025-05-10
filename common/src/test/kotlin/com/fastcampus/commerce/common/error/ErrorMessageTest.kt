package com.fastcampus.commerce.common.error

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ErrorMessageTest : FunSpec(
    {
        test("ErrorMessage 객체를 생성할 수 있다.") {
            val errorCode = CommonErrorCode.SERVER_ERROR
            val expectedCode = errorCode.code
            val expectedMessage = errorCode.message

            val sut = ErrorMessage(errorCode)

            sut.code shouldBe expectedCode
            sut.message shouldBe expectedMessage
        }
    },
)
