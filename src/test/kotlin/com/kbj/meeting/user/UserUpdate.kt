package com.kbj.meeting.user

import com.kbj.meeting.TestUtil
import com.kbj.meeting.constant.NotFoundException
import com.kbj.meeting.controller.UserUpdateRequest
import com.kbj.meeting.repository.UserRepository
import com.kbj.meeting.repository.entity.GenderEnum
import com.kbj.meeting.util.EncryptUtil
import com.kbj.meeting.util.JsonUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put

@SpringBootTest
@DisplayName("API /users Test")
@AutoConfigureMockMvc
class UserUpdate() {
    @Autowired private lateinit var encryptUtil: EncryptUtil

    @Autowired private lateinit var testUtil: TestUtil

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var jsonUtil: JsonUtil

    @Autowired private lateinit var userRepository: UserRepository

    @Test
    @DisplayName("GET /users/{id}")
    fun readUserTest() {
        val user = testUtil.createTestUser(mockMvc, "update_sample", "update_sample")

        val orgDbUser = userRepository.findById(user.id).orElseThrow { NotFoundException() }

        val data =
            UserUpdateRequest(
                password = "update_sample",
                name = "${user.name} update",
                gender = GenderEnum.Male.toString(),
                email = "sample@sample.com",
                phone = "01012345678",
                birth = "2000-01-01",
            )

        // Step 1: Failed without Login
        mockMvc.put("/users/${user.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(data)
        }.andExpect {
            status { isUnauthorized() }
        }

        // Step 2: Success with Login
        val (accessToken) = testUtil.loginTest(mockMvc, "update_sample", "update_sample")

        val response =
            mockMvc.put("/users/${user.id}") {
                contentType = MediaType.APPLICATION_JSON
                content = jsonUtil.stringify(data)
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
            }.andReturn().response

        // Step 3: Password does not exist
        val userData = jsonUtil.parse<Map<String, Any>>(response.contentAsString)
        assert(userData["password"] == null) { "User should not have a password property" }

        // Step 4: Data Updated
        val newDbUser = userRepository.findById(user.id).orElseThrow { NotFoundException() }
        assert(newDbUser.name != orgDbUser.name)
        assert(newDbUser.name == "${user.name} update")
        assert(encryptUtil.comparePassword("update_sample", newDbUser.password) == true)

        // Step 4: Request Other UserId
        mockMvc.get("/users/${1000}") {
            header("Authorization", "Bearer $accessToken")
        }.andExpect {
            status { isBadRequest() }
        }
    }
}
