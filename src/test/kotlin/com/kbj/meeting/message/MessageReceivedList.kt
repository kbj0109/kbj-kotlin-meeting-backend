package com.kbj.meeting.message

import com.kbj.meeting.TestUtil
import com.kbj.meeting.controller.AuthLoginResponse
import com.kbj.meeting.controller.UserResponse
import com.kbj.meeting.repository.MessageRepository
import com.kbj.meeting.util.JsonUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
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

    private lateinit var user1: UserResponse
    private lateinit var user2: UserResponse
    private lateinit var user3: UserResponse
    private lateinit var loginRes1: AuthLoginResponse

    @BeforeEach
    fun setUp(): Unit =
        runBlocking {
            val userList =
                awaitAll(
                    async { testUtil.createTestUser(mockMvc, "message2_list_user1", "message2_list_user1") },
                    async { testUtil.createTestUser(mockMvc, "message2_list_user2", "message2_list_user2") },
                    async { testUtil.createTestUser(mockMvc, "message2_list_user3", "message2_list_user3") },
                )

            user1 = userList[0]
            user2 = userList[1]
            user3 = userList[2]

            awaitAll(
                async { testUtil.sendMessageTest(mockMvc, "message2_list_user2", "message2_list_user2", user1.id) },
                async { testUtil.sendMessageTest(mockMvc, "message2_list_user3", "message2_list_user3", user1.id) },
            )

            loginRes1 = testUtil.loginTest(mockMvc, "message2_list_user1", "message2_list_user1")
        }

    @Test
    @DisplayName("post /messages/received")
    fun sendMessageTest() {
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
