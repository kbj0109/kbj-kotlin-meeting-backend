package com.kbj.meeting.annotation

import com.kbj.meeting.constant.UnauthorizedException
import com.kbj.meeting.util.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

// Set UserAuthGuard Interceptor
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UserAuthGuard(
    val allowEmptyToken: Boolean = false,
    val allowExpiredToken: Boolean = false,
)

@Component
class UserAuthGuardInterceptor(private val jwtUtil: JwtUtil) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) return true
        if (handler.getMethodAnnotation(UserAuthGuard::class.java) == null) return true

        val option = handler.method.getAnnotation(UserAuthGuard::class.java)

        val allowEmptyToken = option.allowEmptyToken
        val allowExpiredToken = option.allowExpiredToken

        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")

        if (allowEmptyToken == false && token.isNullOrEmpty()) {
            throw UnauthorizedException(message = "Login Required")
        }

        if (allowEmptyToken == true && token.isNullOrEmpty()) {
            return true
        }

        val loginUserData = jwtUtil.openUserJwtToken(token as String, allowExpiredToken)

        request.setAttribute("loginUser", loginUserData)

        return true
    }
}
