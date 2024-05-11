package com.kbj.meeting.util

import com.kbj.meeting.constant.ExpiredTokenException
import com.kbj.meeting.constant.InvalidTokenException
import com.kbj.meeting.type.LoginUserData
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import java.nio.charset.StandardCharsets
import java.util.Date

class JwtUtil {
    @Value("\${SecretJwtKey}")
    private lateinit var secretJwtKey: String

    private val jwtKey by lazy { Keys.hmacShaKeyFor(secretJwtKey.toByteArray(StandardCharsets.UTF_8)) }

    private val jwtBuilder = Jwts.builder()
    private val jwtParser = Jwts.parserBuilder()

    fun createUserJwtToken(
        data: LoginUserData,
        expireDate: Date,
    ): String {
        val claims =
            mapOf<String, Any>(
                "userId" to data.userId,
                "username" to data.username,
                "authId" to data.authId,
            )

        return jwtBuilder
            .setClaims(claims)
            .signWith(jwtKey, SignatureAlgorithm.HS256)
            .setExpiration(expireDate)
            .compact()
    }

    fun openUserJwtToken(
        token: String,
        allowExpiredToken: Boolean,
    ): LoginUserData {
        try {
            val claims: Claims =
                jwtParser
                    .setSigningKey(jwtKey)
                    .build()
                    .parseClaimsJws(token)
                    .body

            return LoginUserData(
                userId = claims["userId"]?.toString()?.toLongOrNull() ?: 0,
                username = claims["username"]?.toString() ?: "",
                authId = claims["userId"]?.toString()?.toLongOrNull() ?: 0,
            )
        } catch (e: ExpiredJwtException) {
            if (allowExpiredToken == false) {
                throw ExpiredTokenException()
            }

            return LoginUserData(
                userId = e.claims["userId"]?.toString()?.toLongOrNull() ?: 0,
                username = e.claims["username"]?.toString() ?: "",
                authId = e.claims["authId"]?.toString()?.toLongOrNull() ?: 0,
            )
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }
}
