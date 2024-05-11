package com.kbj.meeting.controller

import com.kbj.meeting.repository.entity.User
import com.kbj.meeting.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {
    @PostMapping
    fun createUser(
        @Valid @RequestBody user: CreateUserDTO,
    ): ResponseEntity<User> {
        println(12345)

        val newUser = userService.createUser(user)

        return ResponseEntity(newUser, HttpStatus.CREATED)
    }
}
