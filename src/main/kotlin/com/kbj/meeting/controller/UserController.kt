package com.kbj.meeting.controller

import com.kbj.meeting.repository.entity.User
import com.kbj.meeting.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {
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

    @PostMapping
    fun createUser(
        @RequestBody user: CreateUserDTO,
    ): ResponseEntity<User> {
        val newUser = userService.createUser(user)
        return ResponseEntity(newUser, HttpStatus.CREATED)
    }
}
