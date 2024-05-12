package com.kbj.meeting.message

import com.kbj.meeting.TestUtil
import com.kbj.meeting.repository.MessageRepository
import com.kbj.meeting.util.JsonUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@DisplayName("API /messages Test")
@AutoConfigureMockMvc
class MessageReceivedList() {
    @Autowired private lateinit var messageRepository: MessageRepository

    @Autowired private lateinit var testUtil: TestUtil

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var jsonUtil: JsonUtil

    @Test
    @DisplayName("post /messages/received")
    fun sendMessageTest() {
        val user1 = testUtil.createTestUser(mockMvc, "message2_list_user1", "message2_list_user1")
        val user2 = testUtil.createTestUser(mockMvc, "message2_list_user2", "message2_list_user2")
        val user3 = testUtil.createTestUser(mockMvc, "message2_list_user3", "message2_list_user3")

        val loginRes1 = testUtil.loginTest(mockMvc, "message2_list_user1", "message2_list_user1")

        testUtil.sendMessageTest(mockMvc, "message2_list_user2", "message2_list_user2", user1.id)
        testUtil.sendMessageTest(mockMvc, "message2_list_user3", "message2_list_user3", user1.id)

        // 1. Fail without login
        mockMvc.get("/messages/received?pageNumber=0&pageSize=1")
            .andExpect {
                status { isUnauthorized() }
            }

        // 2. Success with Login
        val sentListRes =
            mockMvc.get("/messages/received?pageNumber=0&pageSize=1") {
                header("Authorization", "Bearer ${loginRes1.accessToken}")
            }
                .andExpect {
                    status { isOk() }
                }.andReturn().response

        val receivedListData = jsonUtil.parse<Map<String, Any>>(sentListRes.contentAsString)

        val list = messageRepository.findByToUserId(user1.id)

        assert(receivedListData["totalCount"] == list.size)
        assert((receivedListData["list"] as List<*>).size == 1)
    }
}
