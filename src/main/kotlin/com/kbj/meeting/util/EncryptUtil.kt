package com.kbj.meeting.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

class EncryptUtil {
    private val encryptor: PasswordEncoder = BCryptPasswordEncoder(12)

    @Value("\${SecretEncryptKey}")
    private lateinit var secretEncryptKey: String

    fun encryptPassword(password: String): String {
        return encryptor.encode(password + secretEncryptKey)
    }

    fun comparePassword(
        password: String,
        hashPassword: String,
    ): Boolean {
        return encryptor.matches(password + secretEncryptKey, hashPassword)
    }
}
