package com.kbj.meeting.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import java.util.Date

enum class AuthTypeEnum(val value: String) {
    REFRESH_TOKEN("refresh"),
}

@Entity
@Table(name = "auths", schema = "kbj_meeting_backend")
class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var expiredAt: Date? = null

    @Column(nullable = false)
    var userId: Long? = null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: AuthTypeEnum? = null
}
