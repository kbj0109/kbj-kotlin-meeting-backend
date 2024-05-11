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

enum class MessageLevelEnum(val value: Int) {
    Normal(3),
    High(7),
}

enum class MessageStatusEnum(val value: String) {
    Activated("Activated"),
    Deactivated("Deactivated"),
    Accepted("Accepted"),
    Rejected("Rejected"),
}

@Entity
@Table(name = "messages", schema = "kbj_meeting_backend")
class Message(
    fromUserId: Long,
    toUserId: Long,
    messageLevel: MessageLevelEnum,
    messageStatus: MessageStatusEnum,
    text: String,
    reason: String?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column
    var createdAt: Date = Date()

    @Column
    var updatedAt: Date = Date()

    @Column
    var deletedAt: Date? = null

    @Column
    var fromUserId: Long = fromUserId

    @Column
    var toUserId: Long = toUserId

    @Column(columnDefinition = "INT")
    var messageLevel: MessageLevelEnum = messageLevel

    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    var messageStatus: MessageStatusEnum = messageStatus

    @Column
    var text: String = text

    @Column
    var reason: String? = reason
}
