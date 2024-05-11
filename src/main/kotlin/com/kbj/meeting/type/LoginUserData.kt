package com.kbj.meeting.type

// 로그인시 생성되는 JWT 토큰 내용
data class LoginUserData(
    val userId: Long,
    val username: String,
    val authId: Long,
)
