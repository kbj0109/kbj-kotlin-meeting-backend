package com.kbj.meeting.annotation

import com.kbj.meeting.constant.UnauthorizedException
import com.kbj.meeting.type.LoginUserData
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

// Get LoginUser Data as Controller Argument
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginUser

@Component
class LoginUserResolver(private val request: HttpServletRequest) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val isLoginUserData = parameter.parameterType == LoginUserData::class.java
        val isLoginUserAnnotation =
            parameter.hasParameterAnnotation(
                LoginUser::class.java,
            )

        return isLoginUserData && isLoginUserAnnotation
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): LoginUserData? {
        val isOptional = parameter.isOptional()
        val loginUserObject = request.getAttribute("loginUser")

        if (isOptional == false && loginUserObject == null) {
            throw UnauthorizedException(message = "LoginRequired")
        }

        if (loginUserObject != null && loginUserObject is LoginUserData) {
            return loginUserObject
        }

        return null
    }
}
