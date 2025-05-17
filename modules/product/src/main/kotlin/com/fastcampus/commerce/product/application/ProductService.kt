package com.fastcampus.commerce.product.application

import com.fastcampus.commerce.product.application.dto.ProductResult
import com.fastcampus.commerce.product.application.dto.SearchProductQuery
import com.fastcampus.commerce.product.domain.service.CategoryReader
import com.fastcampus.commerce.product.domain.service.ProductReader
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ProductService(
    private val productReader: ProductReader,
    private val categoryReader: CategoryReader,
) {
    @Transactional(readOnly = true)
    fun getProductBy(id: Long): ProductResult {
        val productInfo = productReader.getProductBy(id)
        val categoryInfos = categoryReader.getAllByProductId(id)
        return ProductResult.of(productInfo, categoryInfos)
    }

    @Transactional(readOnly = true)
    fun getProducts(query: SearchProductQuery, pageable: Pageable): Page<ProductResult> {
        val productInfos = productReader.findProducts(query, pageable)

        val productIds = productInfos.content.map { it.id }
        val categoryInfoMap = categoryReader.getAllByProductIds(productIds)
            .groupBy { it.productId }

        return productInfos.map { productInfo ->
            val categoryInfos = categoryInfoMap[productInfo.id]!!
            ProductResult.of(productInfo, categoryInfos)
        }
    }
}
