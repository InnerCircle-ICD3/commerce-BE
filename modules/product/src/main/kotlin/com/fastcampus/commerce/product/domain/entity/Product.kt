package com.fastcampus.commerce.product.domain.entity

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@SQLDelete(sql = "update products set is_deleted = true, deleted_at = now() where id = ?")
@SQLRestriction("is_deleted = false and status = 'ON_SALE'")
@Table(name = "products")
@Entity
class Product(
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var price: Int,
    @Column(nullable = false)
    var thumbnail: String,
    @Column(nullable = false)
    var detailImage: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: SellingStatus = SellingStatus.ON_SALE,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var isDeleted: Boolean = false

    @LastModifiedDate
    @Column(nullable = false)
    lateinit var updatedAt: LocalDateTime

    @Column
    var deletedAt: LocalDateTime? = null

    @OneToMany(mappedBy = "product")
    val productCategories: List<ProductCategory> = mutableListOf()
}
