package com.fastcampus.commerce.product.application.dto

data class SearchProductQuery(
    val name: String? = null,
    val categoryIds: List<Long>? = null,
    val status: String? = null,
)
