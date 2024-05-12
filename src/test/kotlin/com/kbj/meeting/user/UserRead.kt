package com.kbj.meeting.user

import com.kbj.meeting.TestUtil
import com.kbj.meeting.repository.UserRepository
import com.kbj.meeting.util.JsonUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@DisplayName("API /users Test")
@AutoConfigureMockMvc
class UserRead() {
    @Autowired private lateinit var testUtil: TestUtil

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var jsonUtil: JsonUtil

    @Autowired private lateinit var userRepository: UserRepository

    @Test
    @DisplayName("GET /users/{id}")
    fun readUserTest() {
        val user = testUtil.createTestUser(mockMvc, "read_sample", "read_sample")

        // Step 1: Failed without Login
        mockMvc.get("/users/${user.id}")
            .andExpect {
                status { isUnauthorized() }
            }

        // Step 2: Success with Login
        val (accessToken) = testUtil.loginTest(mockMvc, "read_sample", "read_sample")

        val response =
            mockMvc.get("/users/${user.id}") {
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
            }.andReturn().response

        // Step 3: Password does not exist
        val userData = jsonUtil.parse<Map<String, Any>>(response.contentAsString)
        assert(userData["password"] == null) { "User should not have a password property" }

        // Step 4: Request Other UserId
        mockMvc.get("/users/${1000}") {
            header("Authorization", "Bearer $accessToken")
        }.andExpect {
            status { isBadRequest() }
        }
    }
}
