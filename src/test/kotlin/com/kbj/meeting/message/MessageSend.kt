package com.kbj.meeting.message

import com.kbj.meeting.TestUtil
import com.kbj.meeting.controller.MessageSendRequest
import com.kbj.meeting.repository.entity.MessageLevelEnum
import com.kbj.meeting.util.JsonUtil
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
@DisplayName("API /auths Test")
@AutoConfigureMockMvc
class MessageSend() {
    @Autowired private lateinit var testUtil: TestUtil

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var jsonUtil: JsonUtil

    @Test
    @DisplayName("post /messages/send")
    fun sendMessageTest() {
        val user1 = testUtil.createTestUser(mockMvc, "message_user1", "message_user1")
        val user2 = testUtil.createTestUser(mockMvc, "message_user2", "message_user2")

        val loginRes1 = testUtil.loginTest(mockMvc, "message_user1", "message_user1")
        val loginRes2 = testUtil.loginTest(mockMvc, "message_user2", "message_user2")

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
