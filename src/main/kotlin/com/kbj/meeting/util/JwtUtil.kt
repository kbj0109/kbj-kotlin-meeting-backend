package com.kbj.meeting.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import java.nio.charset.StandardCharsets
import java.util.Date

class JwtUtil {
    @Value("\${SecretJwtKey}")
    private lateinit var secretJwtKey: String

    private val jwtBuilder = Jwts.builder()
    private val jwtKey by lazy { Keys.hmacShaKeyFor(secretJwtKey.toByteArray(StandardCharsets.UTF_8)) }

    fun createUserJwtToken(
        data: Map<String, Any>,
        expireDate: Date,
    ): String {
        return jwtBuilder
            .setClaims(data)
            .signWith(jwtKey, SignatureAlgorithm.HS256)
            .setExpiration(expireDate)
            .compact()
    }
}
