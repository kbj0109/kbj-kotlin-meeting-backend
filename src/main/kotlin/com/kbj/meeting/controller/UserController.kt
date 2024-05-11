package com.kbj.meeting.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController {
    @Value("\${DATABASE_HOST}")
    private lateinit var datasourceHost: String

    @Value("\${DATABASE_PORT}")
    private lateinit var databasePort: String

    @GetMapping()
    fun getTest(): String {
        println(datasourceHost)
        println(databasePort)

        return "Hello from users! 111"
    }
}
