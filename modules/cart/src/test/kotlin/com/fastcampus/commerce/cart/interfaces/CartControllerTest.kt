package com.fastcampus.commerce.cart.interfaces

import com.fastcampus.commerce.cart.application.CartService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpStatus

class CartControllerTest : FunSpec(
    {
        val cartService = mockk<CartService>()
        val cartController = CartController(cartService)

        context("장바구니 상품 추가") {
            test("상품을 장바구니에 추가하면 장바구니 아이템 정보를 응답으로 받을 수 있다") {
                // Given
                val userId = 1L
                val productId = 1L
                val quantity = 2
                val request = CartAddRequest(productId = productId, quantity = quantity)

                val cartAddResponse = createCartAddResponse(
                    cartItemId = 1L,
                    productId = productId,
                    quantity = quantity,
                )

                every { cartService.addToCart(userId, request) } returns cartAddResponse

                // When
                val responseEntity = cartController.addToCart(request)

                // Then
                responseEntity.statusCode shouldBe HttpStatus.OK
                val response = responseEntity.body!!

                response.cartItemId shouldBe cartAddResponse.cartItemId
                response.productId shouldBe cartAddResponse.productId
                response.name shouldBe cartAddResponse.name
                response.price shouldBe cartAddResponse.price
                response.quantity shouldBe cartAddResponse.quantity
                response.stockQuantity shouldBe cartAddResponse.stockQuantity
                response.thumbnail shouldBe cartAddResponse.thumbnail
                response.isSoldOut shouldBe cartAddResponse.isSoldOut
                response.isAvailable shouldBe cartAddResponse.isAvailable
                response.requiresQuantityAdjustment shouldBe cartAddResponse.requiresQuantityAdjustment

                verify { cartService.addToCart(userId, request) }
            }
        }
    },
) {
    companion object {
        private fun createCartAddResponse(
            cartItemId: Long,
            productId: Long,
            name: String = "테스트 상품",
            price: Int = 10000,
            quantity: Int = 2,
            stockQuantity: Int = 100,
            thumbnail: String = "thumbnail.jpg",
            isSoldOut: Boolean = false,
            isAvailable: Boolean = true,
            requiresQuantityAdjustment: Boolean = false,
        ): CartAddResponse {
            return CartAddResponse(
                cartItemId = cartItemId,
                productId = productId,
                name = name,
                price = price,
                quantity = quantity,
                stockQuantity = stockQuantity,
                thumbnail = thumbnail,
                isSoldOut = isSoldOut,
                isAvailable = isAvailable,
                requiresQuantityAdjustment = requiresQuantityAdjustment,
            )
        }
    }
}
