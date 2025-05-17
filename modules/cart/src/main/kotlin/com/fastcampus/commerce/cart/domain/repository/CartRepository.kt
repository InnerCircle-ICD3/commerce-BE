package com.fastcampus.commerce.cart.domain.repository

import com.fastcampus.commerce.cart.domain.Cart
import java.util.Optional

interface CartRepository{
    fun save(cart: Cart): Cart

    fun findById(cartId: Long): Optional<Cart>

    fun findByUserIdAndProductId(userId: Long, productId: Long): Optional<Cart>
}
