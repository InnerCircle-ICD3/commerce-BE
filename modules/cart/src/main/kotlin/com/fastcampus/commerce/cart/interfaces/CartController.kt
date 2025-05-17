package com.fastcampus.commerce.cart.interfaces

import com.fastcampus.commerce.cart.application.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/carts")
class CartController(
    private val cartService: CartService,
) {
    @PostMapping
    fun addToCart(
        @RequestBody request: CartAddRequest,
    ): ResponseEntity<CartAddResponse> {
        val userId = 1L

        val response = cartService.addToCart(userId, request)
        return ResponseEntity.ok(response)
    }
}
