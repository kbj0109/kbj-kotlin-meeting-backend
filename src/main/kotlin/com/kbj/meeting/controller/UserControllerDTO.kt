@file:Suppress("ktlint:standard:filename")

package com.kbj.meeting.controller

import com.kbj.meeting.annotation.paramValidator.NumericString
import com.kbj.meeting.annotation.paramValidator.OneOfValues
import com.kbj.meeting.constant.Regex.Companion.DATE_REGEX
import com.kbj.meeting.repository.entity.GenderEnum
import com.kbj.meeting.repository.entity.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.util.Date

data class UserCreateRequest(
    @field:Size(min = 5, max = 30)
    var username: String,
    @field:Size(min = 5, max = 30)
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

data class UserUpdateRequest(
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

data class UserResponse(
    var id: Long,
    var createdAt: Date,
    var updatedAt: Date,
    var deletedAt: Date?,
    var username: String,
    val name: String?,
    val gender: GenderEnum?,
    val email: String?,
    val phone: String?,
    val birth: String?,
) {
    companion object {
        fun fromUser(user: User): UserResponse {
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
    }
}
