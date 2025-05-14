package com.fastcampus.commerce.product.domain

import java.time.LocalDateTime
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    var name : String,

    @Column(nullable = false, length = 20)
    val status : String,

    @Column(nullable = false)
    val price : Int,

    @Column(nullable = false)
    val thumbnail : String,

    @Column(name = "detail_imgae",nullable = false)
    val detailImage : String,

    @Column(name = "is_deleted",nullable = false)
    val isDeleted : Boolean,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime?,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    val productCategories: MutableList<ProductCategory> = mutableListOf()
)
