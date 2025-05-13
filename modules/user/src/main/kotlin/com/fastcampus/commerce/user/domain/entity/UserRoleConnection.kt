package com.fastcampus.commerce.user.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_role_connections",
    uniqueConstraints = [
        // 하나의 유저에겐 하나의 권한 뿐, 권한은 여러 유저에게 동일한 권한이 부여될 수 있음
        UniqueConstraint(columnNames = ["user_id"])
    ]
)
class UserRoleConnection(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val roleId: Long,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val deletedAt: LocalDateTime? = null
)
