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

    fun createUserJwtToken(
        data: Map<String, Any>,
        expireDate: Date,
    ): String {
        val jwtKey = Keys.hmacShaKeyFor(secretJwtKey.toByteArray(StandardCharsets.UTF_8))

        return jwtBuilder
            .setClaims(data)
            .signWith(jwtKey, SignatureAlgorithm.HS256)
            .setExpiration(expireDate)
            .compact()
    }
}
