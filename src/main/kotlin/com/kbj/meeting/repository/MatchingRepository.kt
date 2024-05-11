package com.kbj.meeting.repository

import com.kbj.meeting.repository.entity.Matching
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MatchingRepository : JpaRepository<Matching, Long> {
    fun readOneByUserIdAndMatchingUserId(
        userId: Long,
        matchingUserId: Long,
    ): Matching?
}
