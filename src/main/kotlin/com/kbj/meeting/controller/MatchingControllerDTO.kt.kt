@file:Suppress("ktlint:standard:filename")

package com.kbj.meeting.controller

import com.kbj.meeting.repository.entity.Matching
import java.util.Date

data class MatchingResponse(
    var id: Long,
    var createdAt: Date,
    var userId: Long,
    var matchingUserId: Long,
    var messageId: Long,
    var matchingUser: UserResponse?,
) {
    companion object {
        fun fromMatching(matching: Matching): MatchingResponse {
            return MatchingResponse(
                id = matching.id,
                createdAt = matching.createdAt,
                userId = matching.userId,
                matchingUserId = matching.matchingUserId,
                messageId = matching.messageId,
                matchingUser = matching.matchingUser?.let { UserResponse.fromUser(it) },
            )
        }
    }
}

data class MatchingesResponse(
    var totalCount: Int,
    var list: List<MatchingResponse>,
)
