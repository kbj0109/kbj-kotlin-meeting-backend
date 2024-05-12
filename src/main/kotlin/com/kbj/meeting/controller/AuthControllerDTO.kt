@file:Suppress("ktlint:standard:filename")

package com.kbj.meeting.controller

import jakarta.validation.constraints.Size

data class AuthLoginRequest(
    @field:Size(min = 5, max = 30)
    var username: String,
    @field:Size(min = 5, max = 30)
    var password: String,
)

data class AuthLoginResponse(
    var accessToken: String,
    var refreshToken: String,
)

data class AuthRenewRequest(
    @field:Size(min = 30)
    var refreshToken: String,
)

data class AuthRenewResponse(
    var accessToken: String,
    var refreshToken: String? = null,
)
