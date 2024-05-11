package com.kbj.meeting.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
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
    var id: Long = 0

    @Column
    var createdAt: Date = Date()

    @Column
    var updatedAt: Date = Date()

    @Column
    var deletedAt: Date? = null

    @Column(name = "from_user_id", insertable = false, updatable = false)
    var fromUserId: Long = fromUserId

    @Column(name = "to_user_id", insertable = false, updatable = false)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    var fromUser: User? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    var toUser: User? = null
}
