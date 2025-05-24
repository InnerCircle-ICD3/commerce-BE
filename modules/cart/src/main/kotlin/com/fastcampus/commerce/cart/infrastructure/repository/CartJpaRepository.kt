package com.fastcampus.commerce.cart.infrastructure.repository

import com.fastcampus.commerce.cart.domain.Cart
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface CartJpaRepository : JpaRepository<Cart, Long> {
    fun findByUserIdAndProductId(userId: Long, productId: Long): Optional<Cart>
}
