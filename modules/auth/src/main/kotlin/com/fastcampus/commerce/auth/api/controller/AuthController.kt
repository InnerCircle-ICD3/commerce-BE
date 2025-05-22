package com.fastcampus.commerce.auth.api.controller

import com.fastcampus.commerce.auth.api.service.AuthService
import com.fastcampus.commerce.common.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {

    /**
     * 토큰 재발급
     */
    @PostMapping("/reissue")
    fun reissueToken(@RequestParam("accessToken") accessToken: String): ApiResponse<String> {
        return ApiResponse.success(authService.reissueToken(accessToken))
    }

}
