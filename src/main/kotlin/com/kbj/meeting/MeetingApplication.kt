package com.kbj.meeting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MeetingApplication

fun main(args: Array<String>) {
    runApplication<MeetingApplication>(*args)
}
