package com.kbj.meeting.message

import com.kbj.meeting.TestUtil
import com.kbj.meeting.controller.AuthLoginResponse
import com.kbj.meeting.controller.MessageSendRequest
import com.kbj.meeting.controller.UserResponse
import com.kbj.meeting.repository.entity.MessageLevelEnum
import com.kbj.meeting.util.JsonUtil
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@DisplayName("API /messages Test")
@AutoConfigureMockMvc
class MessageSend() {
    @Autowired private lateinit var testUtil: TestUtil

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var jsonUtil: JsonUtil

    private lateinit var user1: UserResponse
    private lateinit var user2: UserResponse
    private lateinit var loginRes1: AuthLoginResponse
    private lateinit var loginRes2: AuthLoginResponse

    @BeforeEach
    fun setup() {
        user1 = testUtil.createTestUser(mockMvc, "message_user1", "message_user1")
        user2 = testUtil.createTestUser(mockMvc, "message_user2", "message_user2")

        loginRes1 = testUtil.loginTest(mockMvc, "message_user1", "message_user1")
        loginRes2 = testUtil.loginTest(mockMvc, "message_user2", "message_user2")
    }

    @Test
    @DisplayName("post /messages/send")
    fun sendMessageTest() {
        // 1. Fail without login
        val data =
            MessageSendRequest(
                toUserId = user2.id,
                messageLevel = MessageLevelEnum.Normal,
                text = "Hello World",
            )

        mockMvc.post("/messages/send") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(data)
        }.andExpect {
            status { isUnauthorized() }
        }

        // 2. Success with Login
        mockMvc.post("/messages/send") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(data)
            header("Authorization", "Bearer ${loginRes1.accessToken}")
        }.andExpect {
            status { isOk() }
        }

        // 3. Fail with same level message
        mockMvc.post("/messages/send") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(data)
            header("Authorization", "Bearer ${loginRes1.accessToken}")
        }.andExpect {
            status { isBadRequest() }
        }

        // 4. Success with higher level message
        data.messageLevel = MessageLevelEnum.High

        mockMvc.post("/messages/send") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(data)
            header("Authorization", "Bearer ${loginRes1.accessToken}")
        }.andExpect {
            status { isOk() }
        }
    }
}
