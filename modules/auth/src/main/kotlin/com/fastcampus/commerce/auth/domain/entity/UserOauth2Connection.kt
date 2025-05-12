package com.fastcampus.commerce.auth.domain.entity

import com.fastcampus.commerce.user.domain.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_oauth2_connections")
class UserOauth2Connection(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    val provider: Oauth2Provider,

    @Column(name = "oauth2_id", nullable = false)
    val oauth2Id: String,  // Provider에서 제공하는 유저 고유 ID

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime? = null
)
