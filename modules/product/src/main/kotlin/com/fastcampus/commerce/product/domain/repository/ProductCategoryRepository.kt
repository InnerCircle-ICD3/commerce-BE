package com.fastcampus.commerce.product.domain.repository

import com.fastcampus.commerce.product.domain.model.CategoryInfo

interface ProductCategoryRepository {
    fun findCategoriesByProductId(productId: Long): List<CategoryInfo>

    fun findCategoriesByProductIds(productIds: List<Long>): List<CategoryInfo>
}
