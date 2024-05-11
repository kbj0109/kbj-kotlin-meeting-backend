package com.kbj.meeting.util

class RandomUtil {
    private val alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    private val numeric = "0123456789"

    fun createRandomString(length: Int): String {
        return (1..length)
            .map { alphaNumeric.random() }
            .joinToString("")
    }

    fun createRandomNumericString(length: Int): String {
        return (1..length)
            .map { numeric.random() }
            .joinToString("")
    }
}
