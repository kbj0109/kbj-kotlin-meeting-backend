package com.kbj.meeting

import com.kbj.meeting.constant.NotFoundException
import com.kbj.meeting.controller.AuthLoginRequest
import com.kbj.meeting.controller.AuthLoginResponse
import com.kbj.meeting.controller.MessageResponse
import com.kbj.meeting.controller.MessageSendRequest
import com.kbj.meeting.controller.UserCreateRequest
import com.kbj.meeting.controller.UserResponse
import com.kbj.meeting.repository.MessageRepository
import com.kbj.meeting.repository.entity.GenderEnum
import com.kbj.meeting.repository.entity.MessageLevelEnum
import com.kbj.meeting.util.ConvertUtil
import com.kbj.meeting.util.JsonUtil
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date

@Component()
class TestUtil(
    private val jsonUtil: JsonUtil,
    private val convertUtil: ConvertUtil,
    private val messageRepository: MessageRepository,
) {
    fun parseDate(dateStr: String?): Date? {
        if (dateStr == null) return null

        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val temporalAccessor = formatter.parse(dateStr)
        val instant = Instant.from(temporalAccessor)
        return Date.from(instant)
    }

    fun createTestUser(
        mockMvc: MockMvc,
        username: String,
        password: String,
    ): UserResponse {
        val data =
            UserCreateRequest(
                username = username,
                password = password,
                name = "sample",
                gender = GenderEnum.Male.toString(),
                email = "sample@sample.com",
                phone = "01012345678",
                birth = "2000-01-01",
            )

        val result =
            mockMvc.post("/users") {
                contentType = MediaType.APPLICATION_JSON
                content = jsonUtil.stringify(data)
            }.andExpect {
                status { isOk() }
            }.andReturn()

        val user = jsonUtil.parse<Map<String, Any>>(result.response.contentAsString)

        val userResponse =
            UserResponse(
                id = (user["id"] as Int).toLong(),
                createdAt = parseDate(user["createdAt"].toString())!!,
                updatedAt = parseDate(user["updatedAt"].toString())!!,
                deletedAt = user["deletedAt"]?.let { parseDate(it.toString()) },
                username = user["username"] as String,
                name = user["name"] as String?,
                gender = convertUtil.getEnumValueOrNull<GenderEnum>(user["gender"].toString()),
                email = user["email"] as String?,
                phone = user["phone"] as String?,
                birth = user["birth"] as String?,
            )

        return userResponse
    }

    fun loginTest(
        mockMvc: MockMvc,
        username: String,
        password: String,
    ): AuthLoginResponse {
        val loginRes =
            mockMvc.post("/auths/login") {
                contentType = MediaType.APPLICATION_JSON
                content = jsonUtil.stringify(AuthLoginRequest(username, password))
            }.andExpect {
                status { isOk() }
            }.andReturn().response

        val resBody = jsonUtil.parse<Map<String, Any>>(loginRes.contentAsString)

        val accessToken = resBody["accessToken"] as? String
        val refreshToken = resBody["refreshToken"] as? String

        assert(!accessToken.isNullOrBlank()) { "Access Token is missing or empty." }
        assert(!refreshToken.isNullOrBlank()) { "Refresh Token is missing or empty." }

        return AuthLoginResponse(accessToken!!, refreshToken!!)
    }

    fun sendMessageTest(
        mockMvc: MockMvc,
        username: String,
        password: String,
        toUserId: Long,
    ): MessageResponse {
        val (accessToken) = loginTest(mockMvc, username, password)

        val data = MessageSendRequest(toUserId = toUserId, messageLevel = MessageLevelEnum.Normal, text = "Hello World")

        val res =
            mockMvc.post("/messages/send") {
                contentType = MediaType.APPLICATION_JSON
                content = jsonUtil.stringify(data)
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
            }.andReturn().response

        val messageData = jsonUtil.parse<Map<String, Any>>(res.contentAsString)
        val messageId = (messageData["id"] as Int).toLong()
        val message = messageRepository.findById(messageId).orElseThrow { NotFoundException() }

        return MessageResponse.fromMessage(message)
    }
}
