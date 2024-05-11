@file:Suppress("ktlint:standard:filename")

package com.kbj.meeting.controller

import com.kbj.meeting.annotation.paramValidator.NumericString
import com.kbj.meeting.annotation.paramValidator.OneOfValues
import com.kbj.meeting.constant.Regex.Companion.DATE_REGEX
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class CreateUserDTO(
    @field:Size(min = 5, max = 20)
    var username: String,
    @field:Size(min = 5, max = 20)
    var password: String,
    @field:Size(min = 2)
    val name: String,
    @field:OneOfValues(values = ["Male", "Female"])
    val gender: String?,
    @field:Email()
    val email: String?,
    @field:NumericString()
    val phone: String?,
    @field:Pattern(regexp = DATE_REGEX)
    val birth: String?,
)
