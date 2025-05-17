package com.fastcampus.commerce.product.domain.entity

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@SQLDelete(sql = "update product_categories set is_deleted = true, deleted_at = now() where id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "product_categories")
<<<<<<<< HEAD:modules/product/src/main/kotlin/com/fastcampus/commerce/product/domain/ProductCategory.kt
class ProductCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    var product: Product,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,
========
@Entity
class ProductCategory(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
>>>>>>>> f1c55c8 (refactor: 도메인 엔티티 구조 리팩토링 및 Soft Delete 적용):modules/product/src/main/kotlin/com/fastcampus/commerce/product/domain/entity/ProductCategory.kt

    @Column(nullable = false)
    var isDeleted: Boolean = false

<<<<<<<< HEAD:modules/product/src/main/kotlin/com/fastcampus/commerce/product/domain/ProductCategory.kt
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime?
)


========
    @Column
    var deletedAt: LocalDateTime? = null
}
>>>>>>>> f1c55c8 (refactor: 도메인 엔티티 구조 리팩토링 및 Soft Delete 적용):modules/product/src/main/kotlin/com/fastcampus/commerce/product/domain/entity/ProductCategory.kt
