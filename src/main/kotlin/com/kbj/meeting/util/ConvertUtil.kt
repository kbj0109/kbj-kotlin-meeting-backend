package com.kbj.meeting.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class ConvertUtil {
    inline fun <reified T : Enum<T>> getEnumValueOrNull(name: String?): T? =
        enumValues<T>().find { it.name.equals(name) }

    fun getDateFromLocalDateTime(localDateTime: LocalDateTime): Date {
        val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        return Date.from(instant)
    }
}
