package com.kbj.meeting.controller

import com.kbj.meeting.annotation.LoginUser
import com.kbj.meeting.annotation.UserAuthGuard
import com.kbj.meeting.service.MessageService
import com.kbj.meeting.type.LoginUserData
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/messages")
@Tag(name = "message")
class MessageController(
    private val messageService: MessageService,
) {
    @UserAuthGuard()
    @PostMapping("/send")
    @Transactional
    fun sendMessageToUser(
        @Valid @RequestBody data: MessageSendRequest,
        @LoginUser() loginUser: LoginUserData,
    ): MessageResponse {
        val result = this.messageService.sendMessageAfterCheck(loginUser.userId, data)

        return MessageResponse(
            id = result.id!!,
            createdAt = result.createdAt,
            updatedAt = result.updatedAt,
            deletedAt = result.deletedAt,
            fromUserId = result.fromUserId,
            toUserId = result.toUserId,
            messageLevel = result.messageLevel,
            messageStatus = result.messageStatus,
            text = result.text,
            reason = result.reason,
        )
    }
}
