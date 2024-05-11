@file:Suppress("ktlint:standard:max-line-length")

package com.kbj.meeting.repository

import com.kbj.meeting.repository.entity.Message
import com.kbj.meeting.repository.entity.MessageStatusEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    @Query(
        "SELECT m FROM Message m WHERE m.fromUserId = :fromUserId AND m.toUserId = :toUserId AND m.messageStatus = :messageStatus",
    )
    fun readPreviousMessage(
        @Param("fromUserId") fromUserId: Long,
        @Param("toUserId") toUserId: Long,
        @Param("messageStatus") messageStatus: MessageStatusEnum,
    ): Message?

    @Modifying
    @Query(
        "UPDATE Message m SET m.messageStatus = :messageStatus WHERE m.toUserId = :toUserId AND m.fromUserId = :fromUserId",
    )
    fun updatePreviousMessage(
        @Param("toUserId") toUserId: Long,
        @Param("fromUserId") fromUserId: Long,
        @Param("messageStatus") messageStatus: MessageStatusEnum,
    ): Int

    @Modifying
    @Query(
        "UPDATE Message SET messageStatus = :messageStatus, reason = :reason WHERE id = :messageId",
    )
    fun updateMessageStatus(
        @Param("messageId") messageId: Long,
        @Param("messageStatus") messageStatus: MessageStatusEnum,
        @Param("reason") reason: String?,
    ): Int
}
