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
@Table(name = "category_groups")
class CategoryGroupEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 50)
    var title: String,

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime?,

    @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL])
    val categories: MutableList<CategoryEntity> = mutableListOf()
)
