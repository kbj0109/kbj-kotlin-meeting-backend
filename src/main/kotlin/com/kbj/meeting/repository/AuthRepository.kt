package com.kbj.meeting.repository

import com.kbj.meeting.repository.entity.Auth
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository : JpaRepository<Auth, Long>
