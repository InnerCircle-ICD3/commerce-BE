package com.fastcampus.commerce.cart.interfaces

import com.fastcampus.commerce.cart.application.CartService
import com.fastcampus.commerce.common.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cart")
class CartController(
    private val cartService: CartService,
) {
    @PostMapping
    fun addToCart(
        @RequestBody request: CartAddRequest,
    ): ApiResponse<CartAddResponse> {
        /*
        임시로 userId 부여
        USER 모듈 완성 후 수정 예정
         */
        val userId = 1L

        val response = cartService.addToCart(userId, request)
        return ApiResponse.success(response)
    }
}
