package com.fastcampus.commerce.product.domain.model

import com.fastcampus.commerce.product.domain.entity.Inventory
import com.fastcampus.commerce.product.domain.entity.Product
import com.fastcampus.commerce.product.domain.entity.SellingStatus
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class ProductInfo
    @QueryProjection
    constructor(
        val id: Long,
        val name: String,
        val price: Int,
        val quantity: Int,
        val thumbnail: String,
        val detailImage: String,
        val status: SellingStatus,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
    ) {
        companion object {
            fun of(product: Product, inventory: Inventory): ProductInfo {
                return ProductInfo(
                    id = product.id!!,
                    name = product.name,
                    price = product.price,
                    quantity = inventory.quantity,
                    thumbnail = product.thumbnail,
                    detailImage = product.detailImage,
                    status = product.status,
                    createdAt = product.createdAt,
                    updatedAt = product.updatedAt,
                )
            }
        }
    }
