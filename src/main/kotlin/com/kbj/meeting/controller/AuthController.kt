package com.kbj.meeting.controller

import com.kbj.meeting.annotation.LoginUser
import com.kbj.meeting.annotation.UserAuthGuard
import com.kbj.meeting.service.AuthService
import com.kbj.meeting.type.LoginUserData
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
        @LoginUser loginUser: LoginUserData,
    ): AuthRenewResponse {
        val result = authService.renewAccessToken(loginUser.userId, loginUser.authId, data.refreshToken)

        return result
    }
}
