package com.fastcampus.commerce.auth

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.sql.Date
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class TokenProvider(
    private val jwtProperties: JwtProperties,
) {
    private val key: SecretKey = SecretKeySpec(
        Decoders.BASE64.decode(jwtProperties.secret),
        Jwts.SIG.HS512.key().build().algorithm,
    )

    /**
     * 사용자 ID를 포함한 액세스 토큰을 생성합니다.
     *
     * @param userId 사용자의 ID
     *
     * @return 생성된 액세스 토큰
     */
    fun createAccessToken(userId: Long): String {
        // RestClient User로 호출 (?) => User 엔티티 참조

        return createToken(
            { jwt -> jwt.claim("user_id", userId) },
            jwtProperties.accessTokenExpireMinutes,
            ChronoUnit.MINUTES,
        )
    }

    /**
     * 사용자 ID를 포함한 리프레시 토큰을 생성합니다.
     *
     * @param userId 사용자의 ID
     *
     * @return 생성된 리프레시 토큰
     */
    fun createRefreshToken(userId: Long): String {
        return createToken(
            { jwt -> jwt.claim("user_id", userId) },
            jwtProperties.refreshTokenExpireDays,
            ChronoUnit.DAYS,
        )
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 생성합니다.
     *
     * @param refreshToken 리프레시 토큰
     *
     * @return 새로 생성된 액세스 토큰
     */
    fun refreshAccessToken(refreshToken: String): String {

        // 유효성 검증

        val userId = 123L
        return createAccessToken(userId)
    }

    /**
     * 토큰의 유효성을 검증합니다.
     *
     * @param token 검증할 JWT 토큰
     *
     * @return 토큰이 유효하면 true, 그렇지 않으면 false를 반환합니다.
     *
     * @throws ExpiredJwtException    토큰이 만료된 경우
     * @throws JwtException           토큰이 유효하지 않은 경우
     */
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            return true
        } catch (e: ExpiredJwtException) {
            throw IllegalArgumentException("")
        } catch (e: JwtException) {
            throw IllegalArgumentException("")
        }
    }

    /**
     * JWT 토큰에서 인증 정보를 추출합니다.
     *
     * @param token JWT 토큰
     *
     * @return 인증 정보
     */
    fun getAuthentication(token: String): Authentication {
        // 토큰에서 클레임을 추출
        val claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

        val userId = claims["user_id"] as Long

        // 권한 정보를 생성합니다. (실제 애플리케이션에서는 토큰에서 권한 정보를 추출해야 합니다.)
        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

        // 인증 정보 반환
        return UsernamePasswordAuthenticationToken(userId, "", authorities)
    }

    /**
     * 주어진 연산을 사용하여 지정된 클레임들로 JWT 토큰을 생성합니다.
     *
     * @param customizeClaims `JwtBuilder` 객체를 매개변수로 받고, 토큰에 추가 클레임들을 설정하는 람다 함수입니다.
     * @param expirationValueToAdd 토큰의 만료 시간에 추가할 값입니다.
     * @param expirationUnit 만료 시간의 단위입니다.
     *
     * @return 생성된 JWT 토큰을 문자열 형태로 반환합니다.
     */
    private fun createToken(customizeClaims: (JwtBuilder) -> Unit, expirationValueToAdd: Long, expirationUnit: ChronoUnit): String {
        val jwt: JwtBuilder = Jwts.builder()
            // header
            .header()
            .type("JWT")
            .and()

            // payload
            .issuer(jwtProperties.issuer)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(expirationValueToAdd, expirationUnit)))

        customizeClaims(jwt)

        return jwt
            .signWith(key)
            .compact()
    }
}
