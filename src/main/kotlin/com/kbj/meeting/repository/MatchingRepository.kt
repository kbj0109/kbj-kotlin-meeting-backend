package com.kbj.meeting.repository

import com.kbj.meeting.repository.entity.Matching
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MatchingRepository : JpaRepository<Matching, Long> {
    fun readOneByUserIdAndMatchingUserId(
        userId: Long,
        matchingUserId: Long,
    ): Matching?

    @Query("SELECT m FROM Matching m LEFT JOIN User u ON u.id = m.matchingUserId WHERE m.userId = :userId")
    fun readManyWithMatchingUser(userId: Long): List<Matching>

    @Query(
        "SELECT m FROM Matching m LEFT JOIN User u ON u.id = m.matchingUserId " +
            "WHERE m.userId = :userId",
    )
    fun readManyWithMatchingUser2(
        @Param("userId") userId: Long,
        pageRequest: PageRequest,
    ): Page<Matching>

    fun countByUserId(userId: Long): Int
}
