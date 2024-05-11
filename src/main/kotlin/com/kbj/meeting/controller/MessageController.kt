package com.kbj.meeting.controller

import com.kbj.meeting.annotation.LoginUser
import com.kbj.meeting.annotation.UserAuthGuard
import com.kbj.meeting.repository.entity.MessageStatusEnum
import com.kbj.meeting.service.MessageService
import com.kbj.meeting.type.LoginUserData
import com.kbj.meeting.util.ConvertUtil
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/messages")
@Tag(name = "message")
class MessageController(
    private val messageService: MessageService,
    private val convertUtil: ConvertUtil,
) {
    @UserAuthGuard()
    @PostMapping("/send")
    @Transactional
    fun sendMessageToUser(
        @Valid @RequestBody data: MessageSendRequest,
        @LoginUser() loginUser: LoginUserData,
    ): MessageResponse {
        val item = this.messageService.sendMessageAfterCheck(loginUser.userId, data)

        return MessageResponse(
            id = item.id!!,
            createdAt = item.createdAt,
            updatedAt = item.updatedAt,
            deletedAt = item.deletedAt,
            fromUserId = item.fromUserId,
            toUserId = item.toUserId,
            messageLevel = item.messageLevel,
            messageStatus = item.messageStatus,
            text = item.text,
            reason = item.reason,
        )
    }

    @UserAuthGuard()
    @PostMapping("/{messageId}/confirm")
    @Transactional
    fun updateStatus(
        @PathVariable messageId: Long,
        @Valid @RequestBody data: MessageConfirmRequest,
        @LoginUser() loginUser: LoginUserData,
    ): MessageResponse {
        val item =
            this.messageService.updateMessageStatus(
                messageId.toLong(),
                loginUser.userId,
                convertUtil.getEnumValueOrNull<MessageStatusEnum>(data.messageStatus)!!,
                data.reason,
            )

        return MessageResponse(
            id = item.id!!,
            createdAt = item.createdAt,
            updatedAt = item.updatedAt,
            deletedAt = item.deletedAt,
            fromUserId = item.fromUserId,
            toUserId = item.toUserId,
            messageLevel = item.messageLevel,
            messageStatus = item.messageStatus,
            text = item.text,
            reason = item.reason,
        )
    }
}
