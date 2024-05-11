package com.kbj.meeting.config

import com.kbj.meeting.util.ConvertUtil
import com.kbj.meeting.util.EncryptUtil
import com.kbj.meeting.util.JsonUtil
import com.kbj.meeting.util.JwtUtil
import com.kbj.meeting.util.RandomUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UtilConfig {
    @Bean
    fun encryptUtil(): EncryptUtil {
        return EncryptUtil()
    }

    @Bean
    fun convertUtil(): ConvertUtil {
        return ConvertUtil()
    }

    @Bean
    fun jsonUtil(): JsonUtil {
        return JsonUtil()
    }

    @Bean
    fun jwtUtil(): JwtUtil {
        return JwtUtil()
    }

    @Bean
    fun randomUtil(): RandomUtil {
        return RandomUtil()
    }
}
