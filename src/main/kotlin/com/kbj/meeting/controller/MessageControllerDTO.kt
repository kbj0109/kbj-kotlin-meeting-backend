@file:Suppress("ktlint:standard:filename")

package com.kbj.meeting.controller

import com.kbj.meeting.annotation.paramValidator.OneOfValues
import com.kbj.meeting.repository.entity.MessageLevelEnum
import com.kbj.meeting.repository.entity.MessageStatusEnum
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
)

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
)

data class MessagesResponse(
    var totalCount: Int,
    var list: List<MessageUserResponse>,
)
