package com.kbj.meeting.controller

import com.kbj.meeting.annotation.UserAuthGuard
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
        val loginResult = authService.login(data)

        return loginResult
    }

    @UserAuthGuard(allowExpiredToken = true)
    @PostMapping("/renew")
    fun renewAccessToken(
        @RequestBody data: AuthRenewRequest,
        // loginUser: LoginUser?,
    ): AuthRenewResponse {
        println(1111)

        // println(loginUser)

        println(1111)

        val tempUserId: Long = 1
        val tempAuthId: Long = 1

        val renewResult = authService.renewAccessToken(tempUserId, tempAuthId, data.refreshToken)

        return renewResult
    }
}
