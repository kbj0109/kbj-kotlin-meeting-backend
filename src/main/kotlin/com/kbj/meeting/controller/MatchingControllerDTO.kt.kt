@file:Suppress("ktlint:standard:filename")

package com.kbj.meeting.controller

import jakarta.validation.constraints.Size
import java.util.Date

data class MatchingListRequest(
    @field:Size(min = 5, max = 20)
    var username: String,
    @field:Size(min = 5, max = 20)
    var password: String,
)

data class MatchingResponse(
    var id: Long,
    var createdAt: Date,
    var userId: Long,
    var matchingUserId: Long,
    var messageId: Long,
    var matchingUser: UserResponse?,
)

data class MatchingesResponse(
    var totalCount: Int,
    var list: List<MatchingResponse>,
)
