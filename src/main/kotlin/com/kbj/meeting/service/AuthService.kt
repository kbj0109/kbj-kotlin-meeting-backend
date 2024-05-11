package com.kbj.meeting.service

import com.kbj.meeting.constant.BadRequestException
import com.kbj.meeting.constant.NotFoundException
import com.kbj.meeting.controller.AuthLoginRequest
import com.kbj.meeting.controller.AuthLoginResponse
import com.kbj.meeting.repository.AuthRepository
import com.kbj.meeting.repository.UserRepository
import com.kbj.meeting.repository.entity.Auth
import com.kbj.meeting.repository.entity.AuthTypeEnum
import com.kbj.meeting.util.ConvertUtil
import com.kbj.meeting.util.EncryptUtil
import com.kbj.meeting.util.JwtUtil
import com.kbj.meeting.util.RandomUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val encryptUtil: EncryptUtil,
    private val jwtUtil: JwtUtil,
    private val randomUtil: RandomUtil,
    private val convertUtil: ConvertUtil,
) {
    @Value("\${AccessTokenExpireMinutes}")
    private lateinit var accessTokenExpireMinutes: String

    @Value("\${RefreshTokenExpirDates}")
    private lateinit var refreshTokenExpirDates: String

    fun login(data: AuthLoginRequest): AuthLoginResponse {
        val user = userRepository.findByUsername(data.username) ?: throw NotFoundException()

        val isCorrect = encryptUtil.comparePassword(data.password, user.password)
        if (!isCorrect) throw BadRequestException()

        val refreshToken = randomUtil.createRandomString(10)
        val refreshExpireAt = LocalDateTime.now().plusDays(refreshTokenExpirDates.toLong())

        val auth =
            authRepository.save(
                Auth(
                    userId = user.id,
                    authType = AuthTypeEnum.RefreshToken,
                    expiredAt = refreshExpireAt,
                    data = mapOf("refreshToken" to refreshToken),
                ),
            )

        val jwtData =
            mapOf(
                "userId" to user.id,
                "username" to user.username,
                "authId" to auth.id,
            )

        val accessTokenExpireLocalDateTime = LocalDateTime.now().plusMinutes(accessTokenExpireMinutes.toLong())
        val accessToken =
            jwtUtil.createUserJwtToken(
                jwtData,
                convertUtil.getDateFromLocalDateTime(accessTokenExpireLocalDateTime),
            )

        return AuthLoginResponse(accessToken, refreshToken)
    }
}
