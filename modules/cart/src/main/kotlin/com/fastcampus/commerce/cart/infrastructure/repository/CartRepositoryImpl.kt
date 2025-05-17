package com.fastcampus.commerce.cart.infrastructure.repository

import com.fastcampus.commerce.cart.domain.Cart
import com.fastcampus.commerce.cart.domain.repository.CartRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class CartRepositoryImpl(
    private val cartJpaRepository: CartJpaRepository,
) : CartRepository {
    override fun save(cart: Cart): Cart {
        return cartJpaRepository.save(cart)
    }

    override fun findById(cartId: Long): Optional<Cart> {
        return cartJpaRepository.findById(cartId)
    }

    override fun findByUserIdAndProductId(userId: Long, productId: Long): Optional<Cart> {
        return cartJpaRepository.findByUserIdAndProductId(userId, productId)
    }
}
