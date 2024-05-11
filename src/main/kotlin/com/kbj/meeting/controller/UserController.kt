package com.kbj.meeting.controller

import com.kbj.meeting.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {
    @PostMapping
    fun createUser(
        @Valid @RequestBody user: CreateUserRequest,
    ): UserResponse {
        val newUser = userService.createUser(user)

        return UserResponse(
            id = newUser.id,
            createdAt = newUser.createdAt,
            updatedAt = newUser.updatedAt,
            deletedAt = newUser.deletedAt,
            username = newUser.username,
            name = newUser.name,
            gender = newUser.gender,
            email = newUser.email,
            phone = newUser.phone,
            birth = newUser.birth,
        )
    }
}
