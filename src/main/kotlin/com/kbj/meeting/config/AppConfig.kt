package com.kbj.meeting.config

import com.kbj.meeting.annotation.LoginUserResolver
import com.kbj.meeting.annotation.UserAuthGuardInterceptor
import com.kbj.meeting.interceptor.LogInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AppConfig(
    private val loginUserResolver: LoginUserResolver,
    private val authInterceptor: UserAuthGuardInterceptor,
    private val logInterceptor: LogInterceptor,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginUserResolver)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
        registry.addInterceptor(logInterceptor)
    }
}
