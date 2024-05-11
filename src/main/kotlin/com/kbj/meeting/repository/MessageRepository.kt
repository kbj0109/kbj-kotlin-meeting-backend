package com.kbj.meeting.repository

import com.kbj.meeting.repository.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long>
