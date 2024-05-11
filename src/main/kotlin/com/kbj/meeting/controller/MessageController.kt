package com.kbj.meeting.controller

import com.kbj.meeting.annotation.LoginUser
import com.kbj.meeting.annotation.UserAuthGuard
import com.kbj.meeting.repository.entity.MessageStatusEnum
import com.kbj.meeting.service.MessageService
import com.kbj.meeting.type.LoginUserData
import com.kbj.meeting.util.ConvertUtil
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
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
    @SecurityRequirements(
        SecurityRequirement(name = "Authorization"),
    )
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

    @SecurityRequirements(
        SecurityRequirement(name = "Authorization"),
    )
    @UserAuthGuard()
    @PostMapping("/{messageId}/confirm")
    @Transactional
    fun confirmMessage(
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

    @SecurityRequirements(
        SecurityRequirement(name = "Authorization"),
    )
    @UserAuthGuard()
    @GetMapping("/sent")
    @Transactional
    fun readManySentMessages(
        @LoginUser() loginUser: LoginUserData,
    ): MessagesResponse {
        return null as MessagesResponse
    }

    @SecurityRequirements(
        SecurityRequirement(name = "Authorization"),
    )
    @UserAuthGuard()
    @GetMapping("/received")
    @Transactional
    fun readManyReceivedMessages(
        @LoginUser() loginUser: LoginUserData,
    ): MessagesResponse {
        return null as MessagesResponse
    }
}
