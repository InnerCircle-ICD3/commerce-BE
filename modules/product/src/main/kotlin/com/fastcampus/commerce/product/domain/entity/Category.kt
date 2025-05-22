package com.fastcampus.commerce.product.domain.entity

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.LastModifiedDate
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

@SQLDelete(sql = "update categories set is_deleted = true, deleted_at = now() where id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "categories")
@Entity
class Category(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    val group: CategoryGroup,
    @Column(nullable = false, length = 50)
    var name: String,
    @Column(nullable = false)
    var sortOrder: Int = 0,
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
}
