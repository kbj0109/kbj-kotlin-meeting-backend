package com.kbj.meeting.annotation

import com.kbj.meeting.constant.UnauthorizedException
import com.kbj.meeting.util.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UserAuthGuard(
    val allowEmptyToken: Boolean = false,
    val allowExpiredToken: Boolean = false,
)

@Aspect
@Component
class UserAuthGuardAspect(private val request: HttpServletRequest, private val jwtUtil: JwtUtil) {
    @Before("@annotation(userAuthGuard)")
    fun openJwtToken(userAuthGuard: UserAuthGuard) {
        val allowEmptyToken = userAuthGuard.allowEmptyToken
        val allowExpiredToken = userAuthGuard.allowExpiredToken

        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")

        if (allowEmptyToken == false && token.isNullOrEmpty()) {
            throw UnauthorizedException(message = "Login Required")
        }

        if (allowEmptyToken == true && token.isNullOrEmpty()) {
            return
        }

        val loginData = jwtUtil.openUserJwtToken(token as String, allowExpiredToken)

        request.setAttribute("loginUser", loginData)
    }
}
