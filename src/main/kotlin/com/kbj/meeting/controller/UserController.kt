package com.kbj.meeting.controller

import com.kbj.meeting.annotation.LoginUser
import com.kbj.meeting.annotation.UserAuthGuard
import com.kbj.meeting.service.UserService
import com.kbj.meeting.type.LoginUserData
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.apache.coyote.BadRequestException
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
@Tag(name = "users")
class UserController(private val userService: UserService) {
    @PostMapping
    fun createUser(
        @Valid @RequestBody data: CreateUserRequest,
    ): UserResponse {
        val newUser = userService.createUser(data)

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

    @UserAuthGuard()
    @GetMapping("/{userId}")
    fun readUser(
        @PathVariable userId: Long,
        @LoginUser() loginUser: LoginUserData,
    ): UserResponse {
        if (userId != loginUser.userId) throw BadRequestException()

        val user = userService.getUserById(userId)

        return UserResponse(
            id = user.id,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
            deletedAt = user.deletedAt,
            username = user.username,
            name = user.name,
            gender = user.gender,
            email = user.email,
            phone = user.phone,
            birth = user.birth,
        )
    }

    @UserAuthGuard()
    @Transactional
    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
        @Valid @RequestBody data: UpdateeUserRequest,
        @LoginUser() loginUser: LoginUserData,
    ): UserResponse {
        if (userId != loginUser.userId) throw BadRequestException()

        val item = this.userService.updateUser(userId, data)

        return UserResponse(
            id = item.id,
            createdAt = item.createdAt,
            updatedAt = item.updatedAt,
            deletedAt = item.deletedAt,
            username = item.username,
            name = item.name,
            gender = item.gender,
            email = item.email,
            phone = item.phone,
            birth = item.birth,
        )
    }
}
