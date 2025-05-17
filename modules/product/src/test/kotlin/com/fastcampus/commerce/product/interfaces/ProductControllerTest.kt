package com.fastcampus.commerce.product.interfaces

import com.fastcampus.commerce.product.application.ProductService
import com.fastcampus.commerce.product.application.dto.CategoryResult
import com.fastcampus.commerce.product.application.dto.ProductResult
import com.fastcampus.commerce.product.application.dto.SellingStatusResult
import com.fastcampus.commerce.product.domain.entity.SellingStatus
import com.fastcampus.commerce.product.interfaces.request.SearchProductRequest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

class ProductControllerTest : FunSpec(
    {
        val productService = mockk<ProductService>()
        val productController = ProductController(productService)

        context("상품 단건 조회") {
            test("상품 아이디로 상품을 조회하면 상품 정보를 응답으로 받을 수 있다") {
                val productId = 1L
                val productResult = createProductResult(productId)
                every { productService.getProductBy(productId) } returns productResult

                val response = productController.getProduct(productId)

                response.id shouldBe productId
                response.name shouldBe productResult.name
                response.price shouldBe productResult.price
                response.quantity shouldBe productResult.quantity
                response.thumbnail shouldBe productResult.thumbnail
                response.detailImage shouldBe productResult.detailImage
                response.status.code shouldBe productResult.status.code
                response.status.label shouldBe productResult.status.label
                response.intensity.code shouldBe "INTENSITY"
                response.intensity.label shouldBe "강도"
                response.cupSize.code shouldBe "CUP_SIZE"
                response.cupSize.label shouldBe "컵 사이즈"
                response.isSoldOut shouldBe false

                verify { productService.getProductBy(productId) }
            }
        }

        context("상품 목록 조회") {
            test("검색 조건 없이 상품 목록을 조회할 수 있다") {
                val pageable = PageRequest.of(0, 10)
                val request = SearchProductRequest()
                val query = request.toQuery()

                val productResults = listOf(
                    createProductResult(1L),
                    createProductResult(2L),
                )
                val page = PageImpl(productResults, pageable, productResults.size.toLong())
                every { productService.getProducts(query, pageable) } returns page

                val response = productController.getProducts(request, pageable)

                response.totalElements shouldBe 2
                response.content.size shouldBe 2
                response.content[0].id shouldBe 1L
                response.content[1].id shouldBe 2L

                verify { productService.getProducts(query, pageable) }
            }

            test("이름으로 상품 목록을 검색할 수 있다") {
                val pageable = PageRequest.of(0, 10)
                val request = SearchProductRequest(name = "커피")
                val query = request.toQuery()

                val productResults = listOf(
                    createProductResult(1L, name = "아메리카노 커피"),
                    createProductResult(2L, name = "라떼 커피"),
                )
                val page = PageImpl(productResults, pageable, productResults.size.toLong())
                every { productService.getProducts(query, pageable) } returns page

                val response = productController.getProducts(request, pageable)

                response.totalElements shouldBe 2
                response.content.size shouldBe 2
                response.content[0].name shouldBe "아메리카노 커피"
                response.content[1].name shouldBe "라떼 커피"

                verify { productService.getProducts(query, pageable) }
            }

            test("카테고리 ID로 상품 목록을 검색할 수 있다") {
                val pageable = PageRequest.of(0, 10)
                val request = SearchProductRequest(intensityId = 10L)
                val query = request.toQuery()

                val productResults = listOf(
                    createProductResult(1L),
                    createProductResult(2L),
                )
                val page = PageImpl(productResults, pageable, productResults.size.toLong())
                every { productService.getProducts(query, pageable) } returns page

                val response = productController.getProducts(request, pageable)

                response.totalElements shouldBe 2
                response.content.size shouldBe 2

                verify { productService.getProducts(query, pageable) }
            }

            test("판매 상태로 상품 목록을 검색할 수 있다") {
                val pageable = PageRequest.of(0, 10)
                val request = SearchProductRequest(status = "ON_SALE")
                val query = request.toQuery()

                val productResults = listOf(
                    createProductResult(1L, status = SellingStatus.ON_SALE),
                    createProductResult(2L, status = SellingStatus.ON_SALE),
                )
                val page = PageImpl(productResults, pageable, productResults.size.toLong())
                every { productService.getProducts(query, pageable) } returns page

                val response = productController.getProducts(request, pageable)

                response.totalElements shouldBe 2
                response.content.size shouldBe 2
                response.content[0].status.code shouldBe "ON_SALE"
                response.content[1].status.code shouldBe "ON_SALE"

                verify { productService.getProducts(query, pageable) }
            }
        }
    },
) {
    companion object {
        private fun createProductResult(
            id: Long,
            name: String = "테스트 상품",
            price: Int = 10000,
            quantity: Int = 100,
            status: SellingStatus = SellingStatus.ON_SALE,
        ): ProductResult {
            val now = LocalDateTime.now()

            return ProductResult(
                id = id,
                name = name,
                price = price,
                quantity = quantity,
                thumbnail = "thumbnail.jpg",
                detailImage = "detail.jpg",
                status = SellingStatusResult(status.name, status.label),
                categories = listOf(
                    CategoryResult("INTENSITY", 10L, "강도"),
                    CategoryResult("CUP_SIZE", 20L, "컵 사이즈"),
                ),
                createdAt = now,
                updatedAt = now,
            )
        }
    }
}
