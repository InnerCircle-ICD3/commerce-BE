package com.fastcampus.commerce.product.interfaces.response

import com.fastcampus.commerce.product.application.dto.CategoryResult
import com.fastcampus.commerce.product.application.dto.ProductResult
import com.fastcampus.commerce.product.application.dto.SellingStatusResult

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: Int,
    val quantity: Int,
    val thumbnail: String,
    val detailImage: String,
    val status: SellingStatusResponse,
    val intensity: CategoryResponse,
    val cupSize: CategoryResponse,
    val isSoldOut: Boolean,
    val createdAt: String,
    val updatedAt: String,
) {
    companion object {
        fun from(productResult: ProductResult): ProductResponse {
            val categoryMap = productResult.categoryMap

            return ProductResponse(
                id = productResult.id,
                name = productResult.name,
                price = productResult.price,
                quantity = productResult.quantity,
                thumbnail = productResult.thumbnail,
                detailImage = productResult.detailImage,
                status = SellingStatusResponse.from(productResult.status),
                intensity = CategoryResponse.from(categoryMap["INTENSITY"]!!),
                cupSize = CategoryResponse.from(categoryMap["CUP_SIZE"]!!),
                isSoldOut = productResult.quantity <= 0,
                createdAt = productResult.createdAt.toString(),
                updatedAt = productResult.updatedAt.toString(),
            )
        }
    }
}

data class CategoryResponse(
    val code: String,
    val label: String,
) {
    companion object {
        fun from(categoryResponse: CategoryResult): CategoryResponse {
            return CategoryResponse(
                code = categoryResponse.code,
                label = categoryResponse.label,
            )
        }
    }
}

data class SellingStatusResponse(
    val code: String,
    val label: String,
) {
    companion object {
        fun from(status: SellingStatusResult): SellingStatusResponse {
            return SellingStatusResponse(
                code = status.code,
                label = status.label,
            )
        }
    }
}
