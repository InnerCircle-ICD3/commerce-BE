package com.fastcampus.commerce.cart.application

import com.fastcampus.commerce.cart.domain.Cart
import com.fastcampus.commerce.cart.domain.repository.CartRepository
import com.fastcampus.commerce.cart.interfaces.CartAddRequest
import com.fastcampus.commerce.product.domain.entity.BaseEntity
import com.fastcampus.commerce.product.domain.entity.Inventory
import com.fastcampus.commerce.product.domain.entity.Product
import com.fastcampus.commerce.product.domain.entity.SellingStatus
import com.fastcampus.commerce.product.domain.repository.InventoryRepository
import com.fastcampus.commerce.product.domain.repository.ProductRepository
import com.fastcampus.commerce.user.domain.entity.User
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

class CartServiceTest : FunSpec(
    {
        val cartRepository = mockk<CartRepository>()
        val productRepository = mockk<ProductRepository>()
        val inventoryRepository = mockk<InventoryRepository>()
        val cartService = CartService(cartRepository, productRepository, inventoryRepository)

        context("장바구니 상품 추가") {
            test("상품을 장바구니에 추가하면 장바구니 아이템 정보를 응답으로 받을 수 있다") {
                // Given
                val userId = 1L
                val productId = 1L
                val quantity = 2
                val request = CartAddRequest(productId = productId, quantity = quantity)

                val product = createProduct(productId)
                val inventory = createInventory(productId, 100)
                val cart = createCart(userId, product, quantity)

                every { productRepository.findById(productId) } returns Optional.of(product)
                every { inventoryRepository.findByProductId(productId) } returns Optional.of(inventory)
                every { cartRepository.findByUserIdAndProductId(userId, productId) } returns Optional.empty()
                every { cartRepository.save(any()) } returns cart

                // When
                val response = cartService.addToCart(userId, request)

                // Then
                response.cartItemId shouldBe cart.id
                response.productId shouldBe product.id
                response.name shouldBe product.name
                response.price shouldBe product.price
                response.quantity shouldBe cart.quantity
                response.stockQuantity shouldBe inventory.quantity
                response.thumbnail shouldBe product.thumbnail
                response.isSoldOut shouldBe false
                response.isAvailable shouldBe true
                response.requiresQuantityAdjustment shouldBe false

                verify { productRepository.findById(productId) }
                verify { inventoryRepository.findByProductId(productId) }
                verify { cartRepository.findByUserIdAndProductId(userId, productId) }
                verify { cartRepository.save(any()) }
            }

            test("이미 장바구니에 있는 상품을 추가하면 수량이 증가한다") {
                // Given
                val userId = 1L
                val productId = 1L
                val initialQuantity = 2
                val additionalQuantity = 3
                val request = CartAddRequest(productId = productId, quantity = additionalQuantity)

                val product = createProduct(productId)
                val inventory = createInventory(productId, 100)
                val existingCart = createCart(userId, product, initialQuantity)
                val updatedCart = createCart(userId, product, initialQuantity + additionalQuantity)

                every { productRepository.findById(productId) } returns Optional.of(product)
                every { inventoryRepository.findByProductId(productId) } returns Optional.of(inventory)
                every { cartRepository.findByUserIdAndProductId(userId, productId) } returns Optional.of(existingCart)
                every { cartRepository.save(any()) } returns updatedCart

                // When
                val response = cartService.addToCart(userId, request)

                // Then
                response.cartItemId shouldBe updatedCart.id
                response.quantity shouldBe initialQuantity + additionalQuantity

                verify { productRepository.findById(productId) }
                verify { inventoryRepository.findByProductId(productId) }
                verify { cartRepository.findByUserIdAndProductId(userId, productId) }
                verify { cartRepository.save(any()) }
            }

            test("재고보다 많은 수량을 장바구니에 추가하면 requiresQuantityAdjustment가 true가 된다") {
                // Given
                val userId = 1L
                val productId = 1L
                val quantity = 20
                val stockQuantity = 10
                val request = CartAddRequest(productId = productId, quantity = quantity)

                val product = createProduct(productId)
                val inventory = createInventory(productId, stockQuantity)
                val cart = createCart(userId, product, quantity)

                every { productRepository.findById(productId) } returns Optional.of(product)
                every { inventoryRepository.findByProductId(productId) } returns Optional.of(inventory)
                every { cartRepository.findByUserIdAndProductId(userId, productId) } returns Optional.empty()
                every { cartRepository.save(any()) } returns cart

                // When
                val response = cartService.addToCart(userId, request)

                // Then
                response.requiresQuantityAdjustment shouldBe true

                verify { productRepository.findById(productId) }
                verify { inventoryRepository.findByProductId(productId) }
                verify { cartRepository.findByUserIdAndProductId(userId, productId) }
                verify { cartRepository.save(any()) }
            }

            test("재고보다 많은 수량을 장바구니에 추가하면 장바구니에 재고와 같은 값이 저장된다") {
                // Given
                val userId = 1L
                val productId = 1L
                val requestQuantity = 20
                val stockQuantity = 10
                val request = CartAddRequest(productId = productId, quantity = requestQuantity)

                val product = createProduct(productId)
                val inventory = createInventory(productId, stockQuantity)

                // Create a cart with the adjusted quantity (equal to stock quantity)
                val adjustedCart = createCart(userId, product, stockQuantity)

                every { productRepository.findById(productId) } returns Optional.of(product)
                every { inventoryRepository.findByProductId(productId) } returns Optional.of(inventory)
                every { cartRepository.findByUserIdAndProductId(userId, productId) } returns Optional.empty()

                // Capture the cart being saved to verify its quantity
                every { cartRepository.save(any()) } answers {
                    val savedCart = firstArg<Cart>()
                    // Verify the quantity was adjusted to match inventory
                    savedCart.quantity shouldBe stockQuantity
                    adjustedCart
                }

                // When
                val response = cartService.addToCart(userId, request)

                // Then
                response.quantity shouldBe stockQuantity
                response.requiresQuantityAdjustment shouldBe true

                verify { productRepository.findById(productId) }
                verify { inventoryRepository.findByProductId(productId) }
                verify { cartRepository.findByUserIdAndProductId(userId, productId) }
                verify { cartRepository.save(any()) }
            }
        }
    },
) {
    companion object {
        private fun createProduct(id: Long, name: String = "테스트 상품", price: Int = 10000, status: SellingStatus = SellingStatus.ON_SALE): Product {
            val product = Product(
                name = name,
                price = price,
                thumbnail = "thumbnail.jpg",
                detailImage = "detail.jpg",
                status = status,
            )
            // Use reflection to set the id field
            val idField = Product::class.java.getDeclaredField("id")
            idField.isAccessible = true
            idField.set(product, id)

            // Use reflection to set the createdAt field
            val createdAtField = BaseEntity::class.java.getDeclaredField("createdAt")
            createdAtField.isAccessible = true
            createdAtField.set(product, LocalDateTime.now())

            // Use reflection to set the updatedAt field
            val updatedAtField = Product::class.java.getDeclaredField("updatedAt")
            updatedAtField.isAccessible = true
            updatedAtField.set(product, LocalDateTime.now())

            return product
        }

        private fun createInventory(productId: Long, quantity: Int): Inventory {
            // Create a product for the inventory
            val product = createProduct(productId)

            val inventory = Inventory(
                product = product,
                quantity = quantity,
            )

            // Use reflection to set the id field
            val idField = Inventory::class.java.getDeclaredField("id")
            idField.isAccessible = true
            idField.set(inventory, 1L)

            // Use reflection to set the createdAt field
            val createdAtField = BaseEntity::class.java.getDeclaredField("createdAt")
            createdAtField.isAccessible = true
            createdAtField.set(inventory, LocalDateTime.now())

            // Use reflection to set the updatedAt field
            val updatedAtField = Inventory::class.java.getDeclaredField("updatedAt")
            updatedAtField.isAccessible = true
            updatedAtField.set(inventory, LocalDateTime.now())

            return inventory
        }

        private fun createCart(userId: Long, product: Product, quantity: Int): Cart {
            val user = User(
                id = userId,
                externalId = UUID.randomUUID().toString(),
                nickname = "User_$userId",
            )

            return Cart(
                user = user,
                product = product,
                quantity = quantity,
            )
        }
    }
}
