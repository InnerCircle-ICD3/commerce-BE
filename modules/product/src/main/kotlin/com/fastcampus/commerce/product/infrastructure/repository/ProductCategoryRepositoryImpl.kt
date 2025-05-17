package com.fastcampus.commerce.product.infrastructure.repository

import com.fastcampus.commerce.product.domain.model.CategoryInfo
import com.fastcampus.commerce.product.domain.repository.ProductCategoryRepository
import org.springframework.stereotype.Repository

@Repository
class ProductCategoryRepositoryImpl(
    private val productCategoryJpaRepository: ProductCategoryJpaRepository,
) : ProductCategoryRepository {
    override fun findCategoriesByProductId(productId: Long): List<CategoryInfo> {
        return productCategoryJpaRepository.findCategoriesByProductId(productId)
    }

    override fun findCategoriesByProductIds(productIds: List<Long>): List<CategoryInfo> {
        return productCategoryJpaRepository.findCategoriesByProductIds(productIds)
    }
}
