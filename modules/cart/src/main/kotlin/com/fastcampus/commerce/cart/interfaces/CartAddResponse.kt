package com.fastcampus.commerce.cart.interfaces

data class CartAddResponse(
    val cartItemId: Long,
    val productId: Long,
    val name: String,
    val price: Int,
    val quantity: Int,
    val stockQuantity: Int,
    val thumbnail: String,
    val isSoldOut: Boolean,
    val isAvailable: Boolean,
    val requiresQuantityAdjustment: Boolean,
)
