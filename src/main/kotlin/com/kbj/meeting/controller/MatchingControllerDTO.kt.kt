@file:Suppress("ktlint:standard:filename")

package com.kbj.meeting.controller

import java.util.Date

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
