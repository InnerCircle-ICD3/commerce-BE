package com.fastcampus.commerce.cart.interfaces

data class CartAddRequest(
    val productId: Long,
    val quantity: Int,
)
