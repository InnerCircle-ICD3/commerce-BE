package com.fastcampus.commerce.auth.interfaces.web.security.oauth

import com.fastcampus.commerce.auth.infrastructure.client.UserDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomUserPrincipal(
    private val user: UserDto
) : OAuth2User {

    override fun getName(): String = user.id.toString()
    override fun getAttributes(): Map<String, Any> = mapOf(
        "id" to user.id,
        "email" to (user.email ?: ""),
        "nickname" to (user.nickname ?: "")
    )

    override fun getAuthorities(): Collection<GrantedAuthority> =
        user.roles.map { SimpleGrantedAuthority(it) }

    companion object {
        fun of(user: UserDto): CustomUserPrincipal = CustomUserPrincipal(user)
    }
}
