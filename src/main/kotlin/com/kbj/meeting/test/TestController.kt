package com.kbj.meeting.test

import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class TestController {
    @GetMapping()
    fun getTest(): String {
        return "Hello from users!"
    }
}
