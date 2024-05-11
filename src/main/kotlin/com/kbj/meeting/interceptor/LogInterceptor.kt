package com.kbj.meeting.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception

@Component
class LogInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val startTime = System.currentTimeMillis()
        request.setAttribute("startTime", startTime)
        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?,
    ) {
        val endTime = System.currentTimeMillis()
        val startTime = request.getAttribute("startTime") as Long
        val spentTime = endTime - startTime
        val httpMethod = request.method
        val protocol = request.protocol
        val statusCode = response.status
        val requestUrl = request.requestURI

        println("[$protocol] $statusCode $httpMethod $requestUrl $spentTime ms")
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        try {
            val endTime = System.currentTimeMillis()
            val startTime = request.getAttribute("startTime") as Long
            val spentTime = endTime - startTime
            val httpMethod = request.method
            val protocol = request.protocol

            val statusCode = response.status
            val requestUrl = request.requestURI

            println("[$protocol] $statusCode $httpMethod $requestUrl $spentTime ms $ex")
        } catch (ex: Exception) {
            println("Exception occurred in LogInterceptor: ${ex.message}")
        }
    }
}
