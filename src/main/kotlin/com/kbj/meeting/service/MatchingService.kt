package com.kbj.meeting.service

import com.kbj.meeting.repository.MatchingRepository
import com.kbj.meeting.repository.entity.Matching
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class MatchingService(
    private val matchingRepository: MatchingRepository,
) {
    fun readManyAndTotalCountWithUsers(
        userId: Long,
        pageRequest: PageRequest,
    ): Pair<Int, List<Matching>> {
        val result = this.matchingRepository.readManyWithMatchingUser(userId, pageRequest)

        return Pair(result.totalElements.toInt(), result.content)
    }
}
