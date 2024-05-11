package com.kbj.meeting.controller

import com.kbj.meeting.service.MessageService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/messages")
@Tag(name = "message")
class MessageController(
    private val messageService: MessageService,
)
