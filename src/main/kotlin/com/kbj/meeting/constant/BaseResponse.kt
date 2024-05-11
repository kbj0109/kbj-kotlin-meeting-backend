package com.kbj.meeting.constant

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class BaseResponse<T>(
    val statusCode: Int = ResultCode.Success.statusCode,
    val statusMessage: String? = ResultCode.Success.message,
    val responseTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    val data: T? = null,
)
