package com.fastcampus.commerce.product.domain.service

import com.fastcampus.commerce.product.domain.model.CategoryInfo
import com.fastcampus.commerce.product.domain.repository.ProductCategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryReader(
    private val productCategoryRepository: ProductCategoryRepository,
) {
    fun getAllByProductId(productId: Long): List<CategoryInfo> {
        return productCategoryRepository.findCategoriesByProductId(productId)
    }

    fun getAllByProductIds(productIds: List<Long>): List<CategoryInfo> {
        return productCategoryRepository.findCategoriesByProductIds(productIds)
    }
}
