package com.fastcampus.commerce.product.interfaces.request

import com.fastcampus.commerce.product.application.dto.SearchProductQuery

data class SearchProductRequest(
    val name: String? = null,
    val intensityId: Long? = null,
    val cupSizeId: Long? = null,
    val status: String? = null,
) {
    fun toQuery(): SearchProductQuery {
        return SearchProductQuery(
            name = name,
            categoryIds = listOfNotNull(intensityId, cupSizeId),
            status = status,
        )
    }
}
