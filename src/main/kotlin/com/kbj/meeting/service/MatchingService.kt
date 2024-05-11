package com.kbj.meeting.service

import com.kbj.meeting.repository.MatchingRepository
import com.kbj.meeting.repository.MessageRepository
import com.kbj.meeting.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class MatchingService(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val matchingRepository: MatchingRepository,
) {
    //
}
