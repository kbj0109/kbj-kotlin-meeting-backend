package com.kbj.meeting.matching

import com.kbj.meeting.TestUtil
import com.kbj.meeting.controller.MessageConfirmRequest
import com.kbj.meeting.controller.UserResponse
import com.kbj.meeting.repository.MatchingRepository
import com.kbj.meeting.repository.entity.MessageStatusEnum
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
@DisplayName("API /auths Test")
@AutoConfigureMockMvc
class MatchingList() {
    @Autowired private lateinit var matchingRepository: MatchingRepository

    @Autowired private lateinit var testUtil: TestUtil

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var jsonUtil: JsonUtil

    private lateinit var user1: UserResponse
    private lateinit var user2: UserResponse
    private lateinit var user3: UserResponse
    private lateinit var user1AccessToken: String
    private lateinit var user2AccessToken: String
    private lateinit var user3AccessToken: String

    @BeforeEach
    fun setup() {
        user1 = testUtil.createTestUser(mockMvc, "matching_list_user1", "matching_list_user1")
        user2 = testUtil.createTestUser(mockMvc, "matching_list_user2", "matching_list_user2")
        user3 = testUtil.createTestUser(mockMvc, "matching_list_user3", "matching_list_user3")

        user1AccessToken = testUtil.loginTest(mockMvc, "matching_list_user1", "matching_list_user1").accessToken
        user2AccessToken = testUtil.loginTest(mockMvc, "matching_list_user2", "matching_list_user2").accessToken
        user3AccessToken = testUtil.loginTest(mockMvc, "matching_list_user3", "matching_list_user3").accessToken

        val message1 = testUtil.sendMessageTest(mockMvc, "matching_list_user1", "matching_list_user1", user2.id)
        val message2 = testUtil.sendMessageTest(mockMvc, "matching_list_user1", "matching_list_user1", user3.id)

        val data = MessageConfirmRequest(MessageStatusEnum.Accepted.value, "Good to Accept")
        mockMvc.post("/messages/${message1.id}/confirm") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(data)
            header("Authorization", "Bearer $user2AccessToken")
        }.andExpect {
            status { isOk() }
        }

        mockMvc.post("/messages/${message2.id}/confirm") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(data)
            header("Authorization", "Bearer $user3AccessToken")
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    @DisplayName("GET /matchinges")
    fun sendMessageTest() {
        // 1. Fail without login
        mockMvc.get("/matchinges?pageNumber=0&pageSize=1")
            .andExpect {
                status { isUnauthorized() }
            }

        // 2. Success with Login
        val sentListRes =
            mockMvc.get("/matchinges?pageNumber=0&pageSize=1") {
                header("Authorization", "Bearer $user1AccessToken")
            }
                .andExpect {
                    status { isOk() }
                }.andReturn().response

        val sentListData = jsonUtil.parse<Map<String, Any>>(sentListRes.contentAsString)

        val matchingCount = matchingRepository.countByUserId(user1.id)

        assert(sentListData["totalCount"] == matchingCount)
        assert((sentListData["list"] as List<*>).size == 1)
    }
}
