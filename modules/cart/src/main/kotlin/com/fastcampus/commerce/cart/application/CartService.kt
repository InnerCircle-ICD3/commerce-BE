package com.fastcampus.commerce.cart.application

import com.fastcampus.commerce.cart.domain.Cart
import com.fastcampus.commerce.cart.domain.repository.CartRepository
import com.fastcampus.commerce.cart.interfaces.CartAddRequest
import com.fastcampus.commerce.cart.interfaces.CartAddResponse
import com.fastcampus.commerce.common.error.CoreException
import com.fastcampus.commerce.product.domain.entity.SellingStatus
import com.fastcampus.commerce.product.domain.error.ProductErrorCode
import com.fastcampus.commerce.product.domain.repository.InventoryRepository
import com.fastcampus.commerce.product.domain.repository.ProductRepository
import com.fastcampus.commerce.user.domain.entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class CartService(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    private val inventoryRepository: InventoryRepository,
) {
    fun addToCart(userId: Long, request: CartAddRequest): CartAddResponse {
        val productId = request.productId
        var quantity = request.quantity
        var requiresQuantityAdjustment = false

        val product = productRepository.findById(productId)
            .orElseThrow { CoreException(ProductErrorCode.PRODUCT_NOT_FOUND) }

        val inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow { CoreException(ProductErrorCode.INVENTORY_NOT_FOUND) }

        val existingCart = cartRepository.findByUserIdAndProductId(userId, productId)

        if (request.quantity > inventory.quantity) {
            requiresQuantityAdjustment = true
            quantity = inventory.quantity
        }

        val cart = if (existingCart.isPresent) {
            val cartItem = existingCart.get()
            cartItem.quantity += quantity
            cartItem.updatedAt = LocalDateTime.now()
            cartItem
        } else {
            val user = User(
                id = userId,
                externalId = UUID.randomUUID().toString(),
                nickname = "User_$userId",
            )

            Cart(
                user = user,
                product = product,
                quantity = quantity,
            )
        }

        // Save cart
        val savedCart = cartRepository.save(cart)

        val isSoldOut = inventory.quantity <= 0
        val isAvailable = product.status == SellingStatus.ON_SALE

        // Create response
        return CartAddResponse(
            cartItemId = savedCart.id,
            productId = product.id!!,
            name = product.name,
            price = product.price,
            quantity = savedCart.quantity,
            stockQuantity = inventory.quantity,
            thumbnail = product.thumbnail,
            isSoldOut = isSoldOut,
            isAvailable = isAvailable,
            requiresQuantityAdjustment = requiresQuantityAdjustment,
        )
    }
}
