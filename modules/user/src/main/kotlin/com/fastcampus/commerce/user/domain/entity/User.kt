package com.fastcampus.commerce.user.domain.entity

import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val externalId: String,  // 외부 노출용 (Public ID)

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

    val deletedAt: LocalDateTime? = null,
)
