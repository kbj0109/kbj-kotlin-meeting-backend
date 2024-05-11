package com.kbj.meeting.controller

import com.kbj.meeting.service.AuthService
import com.kbj.meeting.service.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auths")
@Tag(name = "auth")
class AuthController(
    private val userService: UserService,
    private val authService: AuthService,
) {
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody data: AuthLoginRequest,
    ): AuthLoginResponse {
        val result = authService.login(data)

        return result
    }
}
