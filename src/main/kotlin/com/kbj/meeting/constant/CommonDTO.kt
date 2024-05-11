@file:Suppress("ktlint:standard:filename")

package com.kbj.meeting.constant

import jakarta.validation.constraints.Min

data class PagingOptionDTO(
    @field:Min(value = 0)
    val pageNumber: Int,
    @field:Min(value = 1)
    val pageSize: Int,
)
