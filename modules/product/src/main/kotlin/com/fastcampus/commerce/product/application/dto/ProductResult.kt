package com.fastcampus.commerce.product.application.dto

import com.fastcampus.commerce.product.domain.entity.SellingStatus
import com.fastcampus.commerce.product.domain.model.CategoryInfo
import com.fastcampus.commerce.product.domain.model.ProductInfo
import java.time.LocalDateTime

data class ProductResult(
    val id: Long,
    val name: String,
    val price: Int,
    val quantity: Int,
    val thumbnail: String,
    val detailImage: String,
    val status: SellingStatusResult,
    val categories: List<CategoryResult>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    val categoryMap = categories.associateBy { it.code.uppercase() }

    companion object {
        fun of(product: ProductInfo, categoryInfos: List<CategoryInfo>): ProductResult {
            return ProductResult(
                id = product.id,
                name = product.name,
                price = product.price,
                quantity = product.quantity,
                thumbnail = product.thumbnail,
                detailImage = product.detailImage,
                status = SellingStatusResult.from(product.status),
                categories = categoryInfos.map { CategoryResult.from(it) },
                createdAt = product.createdAt,
                updatedAt = product.updatedAt,
            )
        }
    }
}

data class CategoryResult(
    val code: String,
    val id: Long,
    val label: String,
) {
    companion object {
        fun from(category: CategoryInfo): CategoryResult {
            return CategoryResult(
                code = category.groupTitle,
                id = category.categoryId,
                label = category.categoryName,
            )
        }
    }
}

data class SellingStatusResult(
    val code: String,
    val label: String,
) {
    companion object {
        fun from(status: SellingStatus): SellingStatusResult {
            return SellingStatusResult(
                code = status.name,
                label = status.label,
            )
        }
    }
}
