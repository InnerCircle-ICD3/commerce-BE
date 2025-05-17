package com.fastcampus.commerce.product.domain.error

import com.fastcampus.commerce.common.error.ErrorCode
import com.fastcampus.commerce.common.logging.LogLevel

enum class ProductErrorCode(
    override val code: String,
    override val message: String,
    override val logLevel: LogLevel,
) : ErrorCode {
    PRODUCT_NOT_FOUND("PRD-001", "상품을 조회할 수 없습니다.", LogLevel.WARN),
    INVENTORY_NOT_FOUND("PRD-002", "재고정보를 조회할 수 없습니다.", LogLevel.ERROR),
}
