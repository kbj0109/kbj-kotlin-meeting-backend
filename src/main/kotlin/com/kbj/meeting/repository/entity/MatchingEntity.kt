package com.kbj.meeting.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
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
    var id: Long? = null

    @Column
    var createdAt: Date = Date()

    @Column
    var userId: Long = userId

    @Column
    var matchingUserId: Long = matchingUserId

    @Column
    var messageId: Long = messageId
}
