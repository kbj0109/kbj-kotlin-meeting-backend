package com.kbj.meeting.config

import com.kbj.meeting.util.EncryptUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UtilConfig {
    @Bean
    fun encryptUtil(): EncryptUtil {
        return EncryptUtil()
    }
}
