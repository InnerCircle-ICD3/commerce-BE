package com.fastcampus.commerce.product.interfaces

import com.fastcampus.commerce.product.application.ProductService
import com.fastcampus.commerce.product.interfaces.request.SearchProductRequest
import com.fastcampus.commerce.product.interfaces.response.ProductResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/products")
@RestController
class ProductController(
    private val productService: ProductService,
) {
    @GetMapping("/{productId}")
    fun getProduct(
        @PathVariable productId: Long,
    ): ProductResponse {
        val productResult = productService.getProductBy(productId)
        return ProductResponse.from(productResult)
    }

    @GetMapping
    fun getProducts(
        @ModelAttribute request: SearchProductRequest,
        pageable: Pageable,
    ): Page<ProductResponse> {
        val productResults = productService.getProducts(request.toQuery(), pageable)
        return productResults.map { ProductResponse.from(it) }
    }
}
