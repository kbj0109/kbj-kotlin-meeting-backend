package com.kbj.meeting.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.Date

enum class AuthTypeEnum(val value: String) {
    RefreshToken("RefreshToken"),
}

@Entity
@Table(name = "auths", schema = "kbj_meeting_backend")
class Auth(
    userId: Long,
    authType: AuthTypeEnum,
    expiredAt: Date,
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
    var expiredAt: Date = expiredAt
}
