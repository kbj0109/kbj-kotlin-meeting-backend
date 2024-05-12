package com.kbj.meeting.message

import com.kbj.meeting.TestUtil
import com.kbj.meeting.controller.MessageConfirmRequest
import com.kbj.meeting.repository.MatchingRepository
import com.kbj.meeting.repository.entity.MessageStatusEnum
import com.kbj.meeting.util.JsonUtil
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@DisplayName("API /messages Test")
@AutoConfigureMockMvc
class MessageConfirm() {
    @Autowired
    private lateinit var matchingRepository: MatchingRepository

    @Autowired private lateinit var testUtil: TestUtil

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var jsonUtil: JsonUtil

    @Test
    @DisplayName("post /messages/confirm")
    fun sendMessageTest() {
        val user1 = testUtil.createTestUser(mockMvc, "message_confirm_user1", "message_confirm_user1")
        val user2 = testUtil.createTestUser(mockMvc, "message_confirm_user2", "message_confirm_user2")
        val loginRes1 = testUtil.loginTest(mockMvc, "message_confirm_user1", "message_confirm_user1")
        val message = testUtil.sendMessageTest(mockMvc, "message_confirm_user2", "message_confirm_user2", user1.id)

        val data = MessageConfirmRequest(MessageStatusEnum.Accepted.value, "Good to Accept")

        // 1. Fail without login
        mockMvc.post("/messages/${message.id}/confirm") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(data)
        }.andExpect {
            status { isUnauthorized() }
        }

        // 2. Success with Login
        mockMvc.post("/messages/${message.id}/confirm") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(data)
            header("Authorization", "Bearer ${loginRes1.accessToken}")
        }.andExpect {
            status { isOk() }
        }

        // 3. Matching Created
        val matching1 = matchingRepository.findByUserIdAndMatchingUserId(user1.id, user2.id)
        val matching2 = matchingRepository.findByUserIdAndMatchingUserId(user2.id, user1.id)

        assertNotNull(matching1)
        assertNotNull(matching2)
    }
}
