@file:Suppress("ktlint:standard:filename")

package com.kbj.meeting.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.Date

@Entity
@Table(name = "matchinges", schema = "kbj_meeting_backend")
class Matching(
    userId: Long,
    matchingUserId: Long,
    messageId: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column
    var createdAt: Date = Date()

    @Column(name = "user_id", insertable = false, updatable = false)
    var userId: Long = userId

    @Column(name = "matching_user_id", insertable = false, updatable = false)
    var matchingUserId: Long = matchingUserId

    @Column(name = "message_id", insertable = false, updatable = false)
    var messageId: Long = messageId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_user_id")
    var matchingUser: User? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    var message: Message? = null
}
