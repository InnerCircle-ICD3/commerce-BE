package com.fastcampus.commerce.product.domain.model

data class CategoryInfo(
    val productId: Long,
    val groupId: Long,
    val groupTitle: String,
    val categoryId: Long,
    val categoryName: String,
    val sortOrder: Int,
)
