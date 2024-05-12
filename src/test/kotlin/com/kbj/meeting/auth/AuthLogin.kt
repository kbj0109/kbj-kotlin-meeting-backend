package com.kbj.meeting.auth

import com.kbj.meeting.TestUtil
import com.kbj.meeting.controller.AuthLoginRequest
import com.kbj.meeting.util.JsonUtil
import com.kbj.meeting.util.JwtUtil
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
class AuthLogin() {
    @Autowired private lateinit var jwtUtil: JwtUtil

    @Autowired private lateinit var testUtil: TestUtil

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var jsonUtil: JsonUtil

    @Test
    @DisplayName("post /auths/login")
    fun loginTest() {
        val user = testUtil.createTestUser(mockMvc, "login_sample", "login_sample")

        // 존재하지 않는 회원의 경우
        val failData1 = AuthLoginRequest("aaaaaaaaa", "login_sample")

        mockMvc.post("/auths/login") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(failData1)
        }.andExpect {
            status { isNotFound() }
        }

        // 비밀번호가 틀린 경우
        val failData2 = AuthLoginRequest("login_sample", "aaaaa")

        mockMvc.post("/auths/login") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(failData2)
        }.andExpect {
            status { isBadRequest() }
        }

        // 성공시 accessToken
        val data = AuthLoginRequest("login_sample", "login_sample")

        val loginRes =
            mockMvc.post("/auths/login") {
                contentType = MediaType.APPLICATION_JSON
                content = jsonUtil.stringify(data)
            }.andExpect {
                status { isOk() }
            }.andReturn().response

        val loginResBody = jsonUtil.parse<Map<String, Any>>(loginRes.contentAsString)

        assert(loginResBody["accessToken"] != null)
        assert(loginResBody["refreshToken"] != null)

        // AccessToken 내용 확인
        val loginUserData = jwtUtil.openUserJwtToken((loginResBody["accessToken"] as String), allowExpiredToken = true)
        assert(user.id == loginUserData.userId)
        assert(user.username == loginUserData.username)
    }
}
