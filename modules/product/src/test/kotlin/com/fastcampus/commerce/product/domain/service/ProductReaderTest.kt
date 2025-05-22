package com.fastcampus.commerce.product.domain.service

import com.fastcampus.commerce.common.error.CoreException
import com.fastcampus.commerce.product.application.dto.SearchProductQuery
import com.fastcampus.commerce.product.domain.entity.Inventory
import com.fastcampus.commerce.product.domain.entity.Product
import com.fastcampus.commerce.product.domain.entity.SellingStatus
import com.fastcampus.commerce.product.domain.error.ProductErrorCode
import com.fastcampus.commerce.product.domain.model.ProductInfo
import com.fastcampus.commerce.product.domain.repository.InventoryRepository
import com.fastcampus.commerce.product.domain.repository.ProductRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.*

class ProductReaderTest : FunSpec(
    {
        val productRepository = mockk<ProductRepository>()
        val inventoryRepository = mockk<InventoryRepository>()
        val productReader = ProductReader(productRepository, inventoryRepository)

        context("상품 단건조회") {
            test("상품 아이디로 상품 단건을 조회하면 상품 정보를 조회할 수 있다") {
                val productId = 1L
                val product = createProduct(productId)
                val inventory = createInventory(productId, product)

                every { productRepository.findById(productId) } returns Optional.of(product)
                every { inventoryRepository.findByProductId(productId) } returns Optional.of(inventory)

                val result = productReader.getProductBy(productId)

                result.id shouldBe productId
                result.name shouldBe product.name
                result.price shouldBe product.price
                result.quantity shouldBe inventory.quantity
                result.thumbnail shouldBe product.thumbnail
                result.detailImage shouldBe product.detailImage
                result.status shouldBe product.status

                verify { productRepository.findById(productId) }
                verify { inventoryRepository.findByProductId(productId) }
            }

            test("유효하지 않은 상품 아이디로 상품을 조회하면 CoreException 예외가 발생한다") {
                val productId = 999L

                every { productRepository.findById(productId) } returns Optional.empty()

                val exception = shouldThrow<CoreException> {
                    productReader.getProductBy(productId)
                }

                exception.errorCode shouldBe ProductErrorCode.PRODUCT_NOT_FOUND

                verify { productRepository.findById(productId) }
            }

            test("상품의 재고정보가 존재하지 않으면 CoreException 예외가 발생한다") {
                val productId = 1L
                val product = createProduct(productId)

                every { productRepository.findById(productId) } returns Optional.of(product)
                every { inventoryRepository.findByProductId(productId) } returns Optional.empty()

                val exception = shouldThrow<CoreException> {
                    productReader.getProductBy(productId)
                }

                exception.errorCode shouldBe ProductErrorCode.INVENTORY_NOT_FOUND

                verify { productRepository.findById(productId) }
                verify { inventoryRepository.findByProductId(productId) }
            }
        }

        context("상품 목록조회") {
            test("상품의 목록을 조회할 수 있다.") {
                val pageable = PageRequest.of(0, 10)
                val query = SearchProductQuery()
                val products = listOf(
                    createProductInfo(1L, "상품1", SellingStatus.ON_SALE),
                    createProductInfo(2L, "상품2", SellingStatus.ON_SALE),
                )
                val page = PageImpl(products, pageable, products.size.toLong())

                every { productRepository.findProducts(query, pageable) } returns page

                val result = productReader.findProducts(query, pageable)

                result.totalElements shouldBe 2
                result.content.size shouldBe 2
                result.content[0].name shouldBe "상품1"
                result.content[1].name shouldBe "상품2"

                verify { productRepository.findProducts(query, pageable) }
            }

            test("상품 이름으로 상품 목록을 조회할 수 있다.") {

                val pageable = PageRequest.of(0, 10)
                val query = SearchProductQuery(name = "상품1")
                val products = listOf(
                    createProductInfo(1L, "상품1", SellingStatus.ON_SALE),
                )
                val page = PageImpl(products, pageable, products.size.toLong())

                every { productRepository.findProducts(query, pageable) } returns page

                val result = productReader.findProducts(query, pageable)

                result.totalElements shouldBe 1
                result.content.size shouldBe 1
                result.content[0].name shouldBe "상품1"

                verify { productRepository.findProducts(query, pageable) }
            }

            test("상품 판매상태로 상품 목록을 조회할 수 있다.") {
                val pageable = PageRequest.of(0, 10)
                val query = SearchProductQuery(status = "UNAVAILABLE")
                val products = listOf(
                    createProductInfo(3L, "상품3", SellingStatus.UNAVAILABLE),
                    createProductInfo(4L, "상품4", SellingStatus.UNAVAILABLE),
                )
                val page = PageImpl(products, pageable, products.size.toLong())

                every { productRepository.findProducts(query, pageable) } returns page

                val result = productReader.findProducts(query, pageable)

                result.totalElements shouldBe 2
                result.content.size shouldBe 2
                result.content[0].status shouldBe SellingStatus.UNAVAILABLE
                result.content[1].status shouldBe SellingStatus.UNAVAILABLE

                verify { productRepository.findProducts(query, pageable) }
            }

            test("상품 카테고리로 상품 목록을 조회할 수 있다.") {
                val pageable = PageRequest.of(0, 10)
                val categoryIds = listOf(1L, 2L)
                val query = SearchProductQuery(categoryIds = categoryIds)
                val products = listOf(
                    createProductInfo(5L, "상품5", SellingStatus.ON_SALE),
                    createProductInfo(6L, "상품6", SellingStatus.ON_SALE),
                )
                val page = PageImpl(products, pageable, products.size.toLong())

                every { productRepository.findProducts(query, pageable) } returns page

                val result = productReader.findProducts(query, pageable)

                result.totalElements shouldBe 2
                result.content.size shouldBe 2

                verify { productRepository.findProducts(query, pageable) }
            }
        }
    },
) {
    companion object {
        private fun createProduct(id: Long): Product {
            val product = Product(
                name = "테스트 상품",
                price = 10000,
                thumbnail = "thumbnail.jpg",
                detailImage = "detail.jpg",
                status = SellingStatus.ON_SALE,
            )

            val idField = Product::class.java.getDeclaredField("id")
            idField.isAccessible = true
            idField.set(product, id)

            val createdAtField = Product::class.java.getSuperclass().getDeclaredField("createdAt")
            createdAtField.isAccessible = true
            createdAtField.set(product, LocalDateTime.now())

            product.updatedAt = LocalDateTime.now()
            return product
        }

        private fun createInventory(productId: Long, product: Product): Inventory {
            val inventory = Inventory(
                product = product,
                quantity = 100,
            )

            val idField = Inventory::class.java.getDeclaredField("id")
            idField.isAccessible = true
            idField.set(inventory, productId)

            val createdAtField = Inventory::class.java.getSuperclass().getDeclaredField("createdAt")
            createdAtField.isAccessible = true
            createdAtField.set(inventory, LocalDateTime.now())

            inventory.updatedAt = LocalDateTime.now()

            return inventory
        }

        private fun createProductInfo(id: Long, name: String, status: SellingStatus): ProductInfo {
            return ProductInfo(
                id = id,
                name = name,
                price = 10000,
                quantity = 100,
                thumbnail = "thumbnail.jpg",
                detailImage = "detail.jpg",
                status = status,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        }
    }
}
