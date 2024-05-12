package com.kbj.meeting.auth

import com.kbj.meeting.TestUtil
import com.kbj.meeting.controller.AuthRenewRequest
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
class AuthRenew() {
    @Autowired private lateinit var jwtUtil: JwtUtil

    @Autowired private lateinit var testUtil: TestUtil

    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var jsonUtil: JsonUtil

    @Test
    @DisplayName("post /auths/renew")
    fun loginTest() {
        val user = testUtil.createTestUser(mockMvc, "renew_sample", "renew_sample")
        val (accessToken, refreshToken) = testUtil.loginTest(mockMvc, "renew_sample", "renew_sample")

        // Access Token 없는 경우
        mockMvc.post("/auths/renew") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(AuthRenewRequest(refreshToken))
        }.andExpect {
            status { isUnauthorized() }
        }

        // 잘못된 Refresh Token의 경우
        mockMvc.post("/auths/renew") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonUtil.stringify(AuthRenewRequest(refreshToken + "wrong"))
            header("Authorization", "Bearer $accessToken")
        }.andExpect {
            status { isUnauthorized() }
        }

        Thread.sleep(2000)

        // 성공시 달라진 Access Token
        val renewRes =
            mockMvc.post("/auths/renew") {
                contentType = MediaType.APPLICATION_JSON
                content = jsonUtil.stringify(AuthRenewRequest(refreshToken))
                header("Authorization", "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
            }.andReturn().response

        val renewResBody = jsonUtil.parse<Map<String, Any>>(renewRes.contentAsString)
        assert(renewResBody["accessToken"] != accessToken)

        val newData = jwtUtil.openUserJwtToken(renewResBody["accessToken"] as String, true)
        assert(newData.userId == user.id)
        assert(newData.username == user.username)
    }
}
