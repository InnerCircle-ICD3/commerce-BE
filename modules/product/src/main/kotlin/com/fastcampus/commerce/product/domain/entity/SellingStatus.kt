package com.fastcampus.commerce.product.domain.entity

enum class SellingStatus(
    val label: String,
) {
    ON_SALE("판매 중"),
    UNAVAILABLE("판매 중지"),
    HIDDEN("숨김"),
}
