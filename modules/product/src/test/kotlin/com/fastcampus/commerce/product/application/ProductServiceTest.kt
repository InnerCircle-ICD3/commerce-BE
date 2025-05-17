package com.fastcampus.commerce.product.application

import com.fastcampus.commerce.product.application.dto.SearchProductQuery
import com.fastcampus.commerce.product.domain.entity.SellingStatus
import com.fastcampus.commerce.product.domain.model.CategoryInfo
import com.fastcampus.commerce.product.domain.model.ProductInfo
import com.fastcampus.commerce.product.domain.service.CategoryReader
import com.fastcampus.commerce.product.domain.service.ProductReader
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

class ProductServiceTest : FunSpec(
    {
        val productReader = mockk<ProductReader>()
        val categoryReader = mockk<CategoryReader>()
        val productService = ProductService(productReader, categoryReader)

        context("상품 단건 조회") {
            test("상품 아이디로 상품을 조회하면 상품과 카테고리 정보를 함께 조회할 수 있다") {
                // given
                val productId = 1L
                val productInfo = createProductInfo(productId)
                val categoryInfos = listOf(
                    createCategoryInfo(productId, "INTENSITY", 10L, "강도"),
                    createCategoryInfo(productId, "CUP_SIZE", 20L, "컵 사이즈"),
                )

                every { productReader.getProductBy(productId) } returns productInfo
                every { categoryReader.getAllByProductId(productId) } returns categoryInfos

                // when
                val result = productService.getProductBy(productId)

                // then
                result.id shouldBe productId
                result.name shouldBe productInfo.name
                result.price shouldBe productInfo.price
                result.quantity shouldBe productInfo.quantity
                result.thumbnail shouldBe productInfo.thumbnail
                result.detailImage shouldBe productInfo.detailImage
                result.status.code shouldBe productInfo.status.name

                result.categories.size shouldBe 2
                result.categories[0].code shouldBe "INTENSITY"
                result.categories[0].id shouldBe 10L
                result.categories[0].label shouldBe "강도"

                result.categories[1].code shouldBe "CUP_SIZE"
                result.categories[1].id shouldBe 20L
                result.categories[1].label shouldBe "컵 사이즈"

                verify { productReader.getProductBy(productId) }
                verify { categoryReader.getAllByProductId(productId) }
            }
        }

        context("상품 목록 조회") {
            test("검색 조건과 페이징 정보로 상품 목록을 조회할 수 있다") {
                // given
                val pageable = PageRequest.of(0, 10)
                val query = SearchProductQuery()

                val productInfos = listOf(
                    createProductInfo(1L),
                    createProductInfo(2L),
                )
                val page = PageImpl(productInfos, pageable, productInfos.size.toLong())

                val categoryInfos = listOf(
                    createCategoryInfo(1L, "INTENSITY", 10L, "강도"),
                    createCategoryInfo(1L, "CUP_SIZE", 20L, "컵 사이즈"),
                    createCategoryInfo(2L, "INTENSITY", 10L, "강도"),
                    createCategoryInfo(2L, "CUP_SIZE", 20L, "컵 사이즈"),
                )

                every { productReader.findProducts(query, pageable) } returns page
                every { categoryReader.getAllByProductIds(listOf(1L, 2L)) } returns categoryInfos

                // when
                val result = productService.getProducts(query, pageable)

                // then
                result.totalElements shouldBe 2
                result.content.size shouldBe 2

                result.content[0].id shouldBe 1L
                result.content[0].categories.size shouldBe 2

                result.content[1].id shouldBe 2L
                result.content[1].categories.size shouldBe 2

                verify { productReader.findProducts(query, pageable) }
                verify { categoryReader.getAllByProductIds(listOf(1L, 2L)) }
            }

            test("검색 조건에 이름이 포함된 경우 이름으로 상품을 검색할 수 있다") {
                // given
                val pageable = PageRequest.of(0, 10)
                val query = SearchProductQuery(name = "커피")

                val productInfos = listOf(
                    createProductInfo(1L, name = "아메리카노 커피"),
                    createProductInfo(2L, name = "라떼 커피"),
                )
                val page = PageImpl(productInfos, pageable, productInfos.size.toLong())

                val categoryInfos = listOf(
                    createCategoryInfo(1L, "INTENSITY", 10L, "강도"),
                    createCategoryInfo(1L, "CUP_SIZE", 20L, "컵 사이즈"),
                    createCategoryInfo(2L, "INTENSITY", 10L, "강도"),
                    createCategoryInfo(2L, "CUP_SIZE", 20L, "컵 사이즈"),
                )

                every { productReader.findProducts(query, pageable) } returns page
                every { categoryReader.getAllByProductIds(listOf(1L, 2L)) } returns categoryInfos

                // when
                val result = productService.getProducts(query, pageable)

                // then
                result.totalElements shouldBe 2
                result.content.size shouldBe 2
                result.content[0].name shouldBe "아메리카노 커피"
                result.content[1].name shouldBe "라떼 커피"

                verify { productReader.findProducts(query, pageable) }
                verify { categoryReader.getAllByProductIds(listOf(1L, 2L)) }
            }

            test("검색 조건에 카테고리 ID가 포함된 경우 카테고리로 상품을 검색할 수 있다") {
                // given
                val pageable = PageRequest.of(0, 10)
                val query = SearchProductQuery(categoryIds = listOf(10L))

                val productInfos = listOf(
                    createProductInfo(1L),
                    createProductInfo(2L),
                )
                val page = PageImpl(productInfos, pageable, productInfos.size.toLong())

                val categoryInfos = listOf(
                    createCategoryInfo(1L, "INTENSITY", 10L, "강도"),
                    createCategoryInfo(1L, "CUP_SIZE", 20L, "컵 사이즈"),
                    createCategoryInfo(2L, "INTENSITY", 10L, "강도"),
                    createCategoryInfo(2L, "CUP_SIZE", 20L, "컵 사이즈"),
                )

                every { productReader.findProducts(query, pageable) } returns page
                every { categoryReader.getAllByProductIds(listOf(1L, 2L)) } returns categoryInfos

                // when
                val result = productService.getProducts(query, pageable)

                // then
                result.totalElements shouldBe 2
                result.content.size shouldBe 2

                verify { productReader.findProducts(query, pageable) }
                verify { categoryReader.getAllByProductIds(listOf(1L, 2L)) }
            }

            test("검색 조건에 상태가 포함된 경우 상태로 상품을 검색할 수 있다") {
                // given
                val pageable = PageRequest.of(0, 10)
                val query = SearchProductQuery(status = "ON_SALE")

                val productInfos = listOf(
                    createProductInfo(1L, status = SellingStatus.ON_SALE),
                    createProductInfo(2L, status = SellingStatus.ON_SALE),
                )
                val page = PageImpl(productInfos, pageable, productInfos.size.toLong())

                val categoryInfos = listOf(
                    createCategoryInfo(1L, "INTENSITY", 10L, "강도"),
                    createCategoryInfo(1L, "CUP_SIZE", 20L, "컵 사이즈"),
                    createCategoryInfo(2L, "INTENSITY", 10L, "강도"),
                    createCategoryInfo(2L, "CUP_SIZE", 20L, "컵 사이즈"),
                )

                every { productReader.findProducts(query, pageable) } returns page
                every { categoryReader.getAllByProductIds(listOf(1L, 2L)) } returns categoryInfos

                // when
                val result = productService.getProducts(query, pageable)

                // then
                result.totalElements shouldBe 2
                result.content.size shouldBe 2
                result.content[0].status.code shouldBe "ON_SALE"
                result.content[1].status.code shouldBe "ON_SALE"

                verify { productReader.findProducts(query, pageable) }
                verify { categoryReader.getAllByProductIds(listOf(1L, 2L)) }
            }
        }
    },
) {
    companion object {
        private fun createProductInfo(
            id: Long,
            name: String = "테스트 상품",
            price: Int = 10000,
            quantity: Int = 100,
            status: SellingStatus = SellingStatus.ON_SALE,
        ): ProductInfo {
            val now = LocalDateTime.now()

            return ProductInfo(
                id = id,
                name = name,
                price = price,
                quantity = quantity,
                thumbnail = "thumbnail.jpg",
                detailImage = "detail.jpg",
                status = status,
                createdAt = now,
                updatedAt = now,
            )
        }

        private fun createCategoryInfo(productId: Long, groupTitle: String, categoryId: Long, categoryName: String): CategoryInfo {
            return CategoryInfo(
                productId = productId,
                groupId = categoryId,
                groupTitle = groupTitle,
                categoryId = categoryId,
                categoryName = categoryName,
                sortOrder = 1,
            )
        }
    }
}
