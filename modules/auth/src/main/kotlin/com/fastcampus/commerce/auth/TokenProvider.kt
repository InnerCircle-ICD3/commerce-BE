package com.fastcampus.commerce.auth

import com.fastcampus.commerce.common.error.AuthErrorCode
import com.fastcampus.commerce.common.error.CoreException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import org.springframework.stereotype.Component
import java.sql.Date
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

private const val USER_ID = "user_id"

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
        return createToken(
            { jwt -> jwt.claim(USER_ID, userId) },
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
            { jwt -> jwt.claim(USER_ID, userId) },
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
        val userId = extractUserIdFromToken(refreshToken)
        return createAccessToken(userId)
    }

    /**
     * JWT 토큰에서 회원 ID를 추출한다.
     *
     * @param token JWT 토큰
     *
     * @return 회원 ID
     */
    fun extractUserIdFromToken(token: String): Long {
        val claims = parseClaims(token)
        return claims[USER_ID] as Long
    }

    /**
     * 주어진 연산을 사용하여 지정된 클레임들로 JWT 토큰을 생성한다.
     *
     * @param customizeClaims `JwtBuilder` 객체를 매개변수로 받고, 토큰에 추가 클레임들을 설정하는 람다 함수
     * @param expirationValueToAdd 토큰의 만료 시간에 추가할 값
     * @param expirationUnit 만료 시간 단위
     *
     * @return 생성된 JWT 토큰을 문자열 형태로 반환
     */
    private fun createToken(
        customizeClaims: (JwtBuilder) -> Unit,
        expirationValueToAdd: Long,
        expirationUnit: ChronoUnit,
    ): String {
        val jwt: JwtBuilder = Jwts.builder()
            // header
            .header().type("JWT").and()

            // payload
            .issuer(jwtProperties.issuer)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(expirationValueToAdd, expirationUnit)))

        customizeClaims(jwt)

        return jwt
            .signWith(key)
            .compact()
    }

    /**
     * 주어진 JWT 토큰에서 클레임(Claims)을 파싱하여 반환한다.
     *
     * @param token 파싱할 JWT 토큰
     *
     * @return JWT 토큰에서 추출한 클레임 정보
     *
     * @throws ExpiredJwtException 토큰이 만료된 경우
     * @throws JwtException        토큰이 유효하지 않은 경우
     */
    private fun parseClaims(token: String): Claims {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (_: ExpiredJwtException) {
            throw CoreException(AuthErrorCode.EXPIRED_TOKEN)
        } catch (_: JwtException) {
            throw CoreException(AuthErrorCode.INVALID_TOKEN)
        }
    }
}
