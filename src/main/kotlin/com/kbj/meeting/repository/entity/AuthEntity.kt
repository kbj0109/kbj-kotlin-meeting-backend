package com.kbj.meeting.repository.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Converter
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.Date

enum class AuthTypeEnum(val value: String) {
    RefreshToken("RefreshToken"),
}

sealed class AuthData {
    data class RefreshToken(val refreshToken: String) : AuthData()
}

@Entity
@Table(name = "auths", schema = "kbj_meeting_backend")
class Auth(
    userId: Long,
    authType: AuthTypeEnum,
    expiredAt: LocalDateTime,
    data: AuthData,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(nullable = false)
    var createdAt: Date? = Date()

    @Column(nullable = false)
    var userId: Long = userId

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var authType: AuthTypeEnum = authType

    @Column()
    var expiredAt: LocalDateTime = expiredAt

    @Column
    @Convert(converter = AuthJsonConverter::class)
    var data: AuthData = data
}

@Converter
class AuthJsonConverter : AttributeConverter<Any?, String?> { // Change the type to Any?
    val objectMapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(data: Any?): String? {
        return objectMapper.writeValueAsString(data)
    }

    override fun convertToEntityAttribute(dbData: String?): Any? {
        if (dbData == null) return null
        return objectMapper.readValue(dbData, AuthData.RefreshToken::class.java)
    }
}
