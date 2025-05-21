package com.fastcampus.commerce.user.domain.entity

import com.fastcampus.commerce.auth.domain.model.Oauth2ProviderType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "oauth2_providers")
class Oauth2Provider(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(length = 50, nullable = false, unique = true)
    val name: Oauth2ProviderType,  // KAKAO, NAVER 등

    @Column(nullable = false)
    val isActive: Boolean = true,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val deletedAt: LocalDateTime? = null
)
