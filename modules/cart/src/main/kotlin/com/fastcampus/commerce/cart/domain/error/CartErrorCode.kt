package com.fastcampus.commerce.cart.domain.error

import com.fastcampus.commerce.common.error.ErrorCode
import com.fastcampus.commerce.common.logging.LogLevel

enum class CartErrorCode(
    override val code: String,
    override val message: String,
    override val logLevel: LogLevel,
) : ErrorCode {
    PRODUCT_SOLD_OUT("CRT-001", "상품을 재고가 없습니다.", LogLevel.WARN),
}
