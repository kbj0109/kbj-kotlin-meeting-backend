package com.kbj.meeting.service

import com.kbj.meeting.constant.BadRequestException
import com.kbj.meeting.constant.ExpiredTokenException
import com.kbj.meeting.constant.InvalidTokenException
import com.kbj.meeting.constant.NotFoundException
import com.kbj.meeting.controller.AuthLoginRequest
import com.kbj.meeting.controller.AuthLoginResponse
import com.kbj.meeting.controller.AuthRenewResponse
import com.kbj.meeting.repository.AuthRepository
import com.kbj.meeting.repository.UserRepository
import com.kbj.meeting.repository.entity.Auth
import com.kbj.meeting.repository.entity.AuthData
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

    @Value("\${RefreshTokenExpireDates}")
    private lateinit var refreshTokenExpireDates: String

    fun login(data: AuthLoginRequest): AuthLoginResponse {
        val user = userRepository.findByUsername(data.username) ?: throw NotFoundException()

        val isCorrect = encryptUtil.comparePassword(data.password, user.password)
        if (!isCorrect) throw BadRequestException()

        val refreshToken = randomUtil.createRandomString(30)
        val refreshExpireAt = LocalDateTime.now().plusDays(refreshTokenExpireDates.toLong())

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

    fun renewAccessToken(
        userId: Long,
        authId: Long,
        refreshToken: String,
    ): AuthRenewResponse {
        val auth = this.authRepository.findById(authId).orElseThrow { NotFoundException() }

        if ((auth.data as AuthData.RefreshToken).refreshToken !== refreshToken) {
            throw InvalidTokenException()
        }

        if (auth.expiredAt < LocalDateTime.now()) {
            throw ExpiredTokenException("Refresh Token Expired")
        }

        val user = userRepository.findById(userId).orElseThrow { NotFoundException() }

        val isRenewRefresh =
            LocalDateTime.now().plusDays(
                refreshTokenExpireDates.toLong() / 3 * 2,
            ) > auth.expiredAt

        var newAuthId = auth.id
        var newRefreshToken = refreshToken

        if (isRenewRefresh) {
            newRefreshToken = randomUtil.createRandomString(30)
            val nweRefreshExpireAt = LocalDateTime.now().plusDays(refreshTokenExpireDates.toLong())

            authRepository.deleteById(auth.id)
            val newAuth =
                authRepository.save(
                    Auth(
                        userId = user.id,
                        authType = AuthTypeEnum.RefreshToken,
                        expiredAt = nweRefreshExpireAt,
                        data = mapOf("refreshToken" to newRefreshToken),
                    ),
                )

            newAuthId = newAuth.id
        }

        val accessTokenExpireLocalDateTime = LocalDateTime.now().plusMinutes(accessTokenExpireMinutes.toLong())
        val accessToken =
            jwtUtil.createUserJwtToken(
                mapOf(
                    "userId" to user.id,
                    "username" to user.username,
                    "authId" to newAuthId,
                ),
                convertUtil.getDateFromLocalDateTime(accessTokenExpireLocalDateTime),
            )

        if (isRenewRefresh) {
            return AuthRenewResponse(accessToken, refreshToken)
        }

        return AuthRenewResponse(accessToken)
    }
}
