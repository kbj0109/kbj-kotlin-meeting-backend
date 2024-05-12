package com.kbj.meeting.user

import com.kbj.meeting.controller.UserCreateRequest
import com.kbj.meeting.repository.UserRepository
import com.kbj.meeting.repository.entity.GenderEnum
import com.kbj.meeting.util.JsonUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@DisplayName("API /users Test")
@AutoConfigureMockMvc
class UserCreate() {
    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var jsonUtil: JsonUtil

    @Autowired private lateinit var userRepository: UserRepository

    @Test
    @Order(1)
    @DisplayName("POST /users")
    fun createUserTest() {
        val data =
            UserCreateRequest(
                username = "sample",
                password = "sample",
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

        // 1. check user doesn't have password property
        assert(user["password"] == null) { "User should not have a password property" }

        // 2. check dbUser.username = data.username
        val dbUser = userRepository.findById((user["id"]as Int).toLong()).orElseThrow { NotFoundException() }
        assert(dbUser.username == data.username) { "Database user's username should match the data" }

        // 3. Conflict Exception for duplicate username
        mockMvc.post("/users") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(data)
        }.andExpect {
            status { isConflict() }
        }
    }
}
