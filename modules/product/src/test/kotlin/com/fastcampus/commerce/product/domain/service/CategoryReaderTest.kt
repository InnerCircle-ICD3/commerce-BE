package com.fastcampus.commerce.product.domain.service

import com.fastcampus.commerce.product.domain.model.CategoryInfo
import com.fastcampus.commerce.product.domain.repository.ProductCategoryRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CategoryReaderTest : FunSpec(
    {
        val productCategoryRepository = mockk<ProductCategoryRepository>()
        val categoryReader = CategoryReader(productCategoryRepository)

        context("카테고리 조회") {
            test("상품 아이디로 카테고리 목록을 조회할 수 있다") {
                val productId = 1L
                val categoryInfos: List<CategoryInfo> = listOf(
                    createCategoryInfo(productId, 1L, "그룹1", 10L, "카테고리1", 1),
                    createCategoryInfo(productId, 2L, "그룹2", 20L, "카테고리2", 2),
                )
                every { productCategoryRepository.findCategoriesByProductId(productId) } returns categoryInfos

                val result = categoryReader.getAllByProductId(productId)

                result.size shouldBe 2
                result[0].productId shouldBe productId
                result[0].groupId shouldBe 1L
                result[0].groupTitle shouldBe "그룹1"
                result[0].categoryId shouldBe 10L
                result[0].categoryName shouldBe "카테고리1"
                result[0].sortOrder shouldBe 1

                result[1].productId shouldBe productId
                result[1].groupId shouldBe 2L
                result[1].groupTitle shouldBe "그룹2"
                result[1].categoryId shouldBe 20L
                result[1].categoryName shouldBe "카테고리2"
                result[1].sortOrder shouldBe 2

                verify { productCategoryRepository.findCategoriesByProductId(productId) }
            }

            test("상품 아이디 목록으로 카테고리 목록을 조회할 수 있다") {
                val productIds: List<Long> = listOf(1L, 2L)
                val categoryInfos: List<CategoryInfo> = listOf(
                    createCategoryInfo(1L, 1L, "그룹1", 10L, "카테고리1", 1),
                    createCategoryInfo(1L, 2L, "그룹2", 20L, "카테고리2", 2),
                    createCategoryInfo(2L, 1L, "그룹1", 10L, "카테고리1", 1),
                    createCategoryInfo(2L, 3L, "그룹3", 30L, "카테고리3", 3),
                )
                every { productCategoryRepository.findCategoriesByProductIds(productIds) } returns categoryInfos

                val result = categoryReader.getAllByProductIds(productIds)

                result.size shouldBe 4

                result[0].productId shouldBe 1L
                result[0].groupId shouldBe 1L
                result[0].groupTitle shouldBe "그룹1"
                result[0].categoryId shouldBe 10L
                result[0].categoryName shouldBe "카테고리1"
                result[0].sortOrder shouldBe 1

                result[1].productId shouldBe 1L
                result[1].groupId shouldBe 2L
                result[1].groupTitle shouldBe "그룹2"
                result[1].categoryId shouldBe 20L
                result[1].categoryName shouldBe "카테고리2"
                result[1].sortOrder shouldBe 2

                result[2].productId shouldBe 2L
                result[2].groupId shouldBe 1L
                result[2].groupTitle shouldBe "그룹1"
                result[2].categoryId shouldBe 10L
                result[2].categoryName shouldBe "카테고리1"
                result[2].sortOrder shouldBe 1

                result[3].productId shouldBe 2L
                result[3].groupId shouldBe 3L
                result[3].groupTitle shouldBe "그룹3"
                result[3].categoryId shouldBe 30L
                result[3].categoryName shouldBe "카테고리3"
                result[3].sortOrder shouldBe 3

                verify { productCategoryRepository.findCategoriesByProductIds(productIds) }
            }
        }
    },
) {
    companion object {
        private fun createCategoryInfo(
            productId: Long,
            groupId: Long,
            groupTitle: String,
            categoryId: Long,
            categoryName: String,
            sortOrder: Int,
        ): CategoryInfo {
            return CategoryInfo(
                productId = productId,
                groupId = groupId,
                groupTitle = groupTitle,
                categoryId = categoryId,
                categoryName = categoryName,
                sortOrder = sortOrder,
            )
        }
    }
}
