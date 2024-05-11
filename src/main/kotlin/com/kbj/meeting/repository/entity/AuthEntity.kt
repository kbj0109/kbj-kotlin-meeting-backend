package com.kbj.meeting.repository.entity

import com.kbj.meeting.util.JsonUtil
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

class AuthData {
    data class RefreshToken(val refreshToken: String)
}

@Entity
@Table(name = "auths", schema = "kbj_meeting_backend")
class Auth(
    userId: Long,
    authType: AuthTypeEnum,
    expiredAt: LocalDateTime,
    data: Any,
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
    @Convert(converter = MapToJsonConverter::class)
    var data: Any? = data
}

@Converter
class MapToJsonConverter : AttributeConverter<Any?, String?> { // Change the type to Any?
    private val jsonUtil by lazy { JsonUtil() } // Lazily initialize JsonUtil

    override fun convertToDatabaseColumn(attribute: Any?): String? {
        return jsonUtil.stringify(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): Any? {
        if (dbData == null) return {}
        return jsonUtil.parse(dbData)
    }
}
