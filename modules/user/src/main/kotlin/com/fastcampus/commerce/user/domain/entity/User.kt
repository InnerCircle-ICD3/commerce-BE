package com.fastcampus.commerce.user.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val externalId: String,  // UUID 등 외부 참조 ID

    @Column(length = 100)
    val name: String? = null,

    @Column(length = 256)
    val email: String? = null,

    @Column(length = 50, nullable = false)
    val nickname: String,

    @Column(nullable = false)
    val isDeleted: Boolean = false,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val updatedAt: LocalDateTime? = null,

    val deletedAt: LocalDateTime? = null
)
