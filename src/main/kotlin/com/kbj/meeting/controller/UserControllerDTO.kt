@file:Suppress("ktlint:standard:filename")

package com.kbj.meeting.controller

import com.kbj.meeting.repository.entity.GenderEnum

data class CreateUserDTO(
    val username: String,
    val password: String,
    val name: String,
    val gender: GenderEnum?,
    val email: String?,
    val phone: String?,
    val birth: String?,
)
