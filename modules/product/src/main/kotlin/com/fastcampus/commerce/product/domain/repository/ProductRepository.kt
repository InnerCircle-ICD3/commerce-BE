package com.fastcampus.commerce.product.domain.repository

import com.fastcampus.commerce.product.application.dto.SearchProductQuery
import com.fastcampus.commerce.product.domain.entity.Product
import com.fastcampus.commerce.product.domain.model.ProductInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

interface ProductRepository {
    fun findById(productId: Long): Optional<Product>

    fun findProducts(query: SearchProductQuery, pageable: Pageable): Page<ProductInfo>
}
