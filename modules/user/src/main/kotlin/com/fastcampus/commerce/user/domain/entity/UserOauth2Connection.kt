package com.fastcampus.commerce.user.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_oauth2_connections")
class UserOauth2Connection(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val userId: Long,

    val providerId: Long,

    @Column(length = 255, nullable = false)
    val oauth2Id: String,  // Provider에서 제공하는 유저 고유 ID

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val deletedAt: LocalDateTime? = null
)
