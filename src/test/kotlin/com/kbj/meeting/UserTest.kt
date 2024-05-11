package com.kbj.meeting

import com.kbj.meeting.controller.UserCreateRequest
import com.kbj.meeting.repository.entity.GenderEnum
import com.kbj.meeting.util.JsonUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@DisplayName("API /users Test")
@AutoConfigureMockMvc
class UserTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jsonUtil: JsonUtil

    @Test
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

        mockMvc.post("/users") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(data)
        }.andExpect {
            status { isOk() }
        }
    }
}
