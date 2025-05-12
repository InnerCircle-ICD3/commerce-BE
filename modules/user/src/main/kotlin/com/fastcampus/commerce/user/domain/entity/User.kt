package com.fastcampus.commerce.user.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "external_id", nullable = false, unique = true)
    val externalId: String,  // UUID 등 외부 참조 ID

    @Column(length = 100)
    val name: String? = null,

    @Column(length = 256)
    val email: String? = null,

    @Column(nullable = false, length = 50)
    val nickname: String,

    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean = false,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null,

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime? = null
)
