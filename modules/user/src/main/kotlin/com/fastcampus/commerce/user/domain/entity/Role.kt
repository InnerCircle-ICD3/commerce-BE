package com.fastcampus.commerce.user.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(length = 20, nullable = false, unique = true)
    val code: String,  // ex: USER, ADMIN

    @Column(length = 100)
    val description: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime? = null
)
