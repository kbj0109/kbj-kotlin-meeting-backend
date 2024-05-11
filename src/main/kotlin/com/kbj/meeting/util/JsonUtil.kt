package com.kbj.meeting.util

import com.fasterxml.jackson.databind.ObjectMapper

class JsonUtil {
    val objectMapper = ObjectMapper()

    inline fun <reified T> parse(json: String): T {
        return objectMapper.readValue(json, T::class.java)
    }

    fun <T> stringify(obj: T): String {
        return objectMapper.writeValueAsString(obj)
    }
}
