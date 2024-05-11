package com.kbj.meeting.controller

import com.kbj.meeting.annotation.LoginUser
import com.kbj.meeting.annotation.UserAuthGuard
import com.kbj.meeting.service.UserService
import com.kbj.meeting.type.LoginUserData
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityRequirements
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
        @Valid @RequestBody data: UserCreateRequest,
    ): UserResponse {
        val item = userService.createUser(data)

        return UserResponse.fromUser(item)
    }

    @SecurityRequirements(SecurityRequirement(name = "Authorization"))
    @UserAuthGuard()
    @GetMapping("/{userId}")
    fun readUser(
        @PathVariable userId: Long,
        @LoginUser() loginUser: LoginUserData,
    ): UserResponse {
        if (userId != loginUser.userId) throw BadRequestException()

        val item = userService.getUserById(userId)

        return UserResponse.fromUser(item)
    }

    @SecurityRequirements(SecurityRequirement(name = "Authorization"))
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

        return UserResponse.fromUser(item)
    }
}
