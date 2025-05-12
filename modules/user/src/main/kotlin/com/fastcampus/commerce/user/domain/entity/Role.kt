package com.fastcampus.commerce.user.domain.entity

import com.fastcampus.commerce.user.domain.model.RoleType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val code: RoleType,  // ex: USER, ADMIN

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val deletedAt: LocalDateTime? = null
)
