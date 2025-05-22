package com.fastcampus.commerce.product.domain.service

import com.fastcampus.commerce.common.error.CoreException
import com.fastcampus.commerce.product.application.dto.SearchProductQuery
import com.fastcampus.commerce.product.domain.error.ProductErrorCode
import com.fastcampus.commerce.product.domain.model.ProductInfo
import com.fastcampus.commerce.product.domain.repository.InventoryRepository
import com.fastcampus.commerce.product.domain.repository.ProductRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class ProductReader(
    private val productRepository: ProductRepository,
    private val inventoryRepository: InventoryRepository,
) {
    fun getProductBy(productId: Long): ProductInfo {
        val product = productRepository.findById(productId)
            .orElseThrow { CoreException(ProductErrorCode.PRODUCT_NOT_FOUND) }

        val inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow { CoreException(ProductErrorCode.INVENTORY_NOT_FOUND) }

        return ProductInfo.of(product, inventory)
    }

    fun findProducts(query: SearchProductQuery, pageable: Pageable): Page<ProductInfo> {
        return productRepository.findProducts(query, pageable)
    }
}
