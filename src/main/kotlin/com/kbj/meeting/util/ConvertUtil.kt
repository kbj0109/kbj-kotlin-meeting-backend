package com.kbj.meeting.util

class ConvertUtil {
    inline fun <reified T : Enum<T>> getEnumValueOrNull(name: String?): T? =
        enumValues<T>().find { it.name.equals(name) }
}
