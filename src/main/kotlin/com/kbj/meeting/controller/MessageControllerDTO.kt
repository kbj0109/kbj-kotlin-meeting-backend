@file:Suppress("ktlint:standard:filename")

package com.kbj.meeting.controller

import com.kbj.meeting.annotation.paramValidator.OneOfValues
import com.kbj.meeting.repository.entity.Message
import com.kbj.meeting.repository.entity.MessageLevelEnum
import com.kbj.meeting.repository.entity.MessageStatusEnum
import com.kbj.meeting.repository.entity.User
import jakarta.validation.constraints.Size
import java.util.Date

data class MessageSendRequest(
    var toUserId: Long,
    var messageLevel: MessageLevelEnum,
    @field:Size(min = 5)
    val text: String,
)

data class MessageConfirmRequest(
    @field:OneOfValues(values = ["Accepted", "Rejected" ])
    var messageStatus: String,
    var reason: String?,
)

data class MessageResponse(
    var id: Long,
    var createdAt: Date,
    var updatedAt: Date,
    var deletedAt: Date?,
    var fromUserId: Long,
    var toUserId: Long,
    var messageLevel: MessageLevelEnum,
    var messageStatus: MessageStatusEnum,
    var text: String,
    var reason: String?,
) {
    companion object {
        fun fromMessage(message: Message): MessageResponse {
            return MessageResponse(
                id = message.id,
                createdAt = message.createdAt,
                updatedAt = message.updatedAt,
                deletedAt = message.deletedAt,
                fromUserId = message.fromUserId,
                toUserId = message.toUserId,
                messageLevel = message.messageLevel,
                messageStatus = message.messageStatus,
                text = message.text,
                reason = message.reason,
            )
        }
    }
}

data class MessageUserResponse(
    var id: Long,
    var createdAt: Date,
    var updatedAt: Date,
    var deletedAt: Date?,
    var fromUserId: Long,
    var toUserId: Long,
    var messageLevel: MessageLevelEnum,
    var messageStatus: MessageStatusEnum,
    var text: String,
    var reason: String?,
    var user: UserResponse?,
) {
    companion object {
        fun fromMessageWithUser(
            message: Message,
            user: User?,
        ): MessageUserResponse {
            return MessageUserResponse(
                id = message.id ?: 0,
                createdAt = message.createdAt,
                updatedAt = message.updatedAt,
                deletedAt = message.deletedAt,
                fromUserId = message.fromUserId,
                toUserId = message.toUserId,
                messageLevel = message.messageLevel,
                messageStatus = message.messageStatus,
                text = message.text,
                reason = message.reason,
                user = user?.let { UserResponse.fromUser(it) },
            )
        }
    }
}

data class MessagesResponse(
    var totalCount: Int,
    var list: List<MessageUserResponse>,
)
