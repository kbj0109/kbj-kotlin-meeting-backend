package com.kbj.meeting.controller

import com.kbj.meeting.annotation.LoginUser
import com.kbj.meeting.annotation.UserAuthGuard
import com.kbj.meeting.constant.PagingOptionDTO
import com.kbj.meeting.service.MatchingService
import com.kbj.meeting.type.LoginUserData
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/matchinges")
@Tag(name = "matchinges")
class MatchingController(private val matchingService: MatchingService) {
    @SecurityRequirements(SecurityRequirement(name = "Authorization"))
    @UserAuthGuard()
    @GetMapping()
    fun readManyMatchinges(
        @Valid pagingOption: PagingOptionDTO,
        @LoginUser() loginUser: LoginUserData,
    ): MatchingesResponse {
        val pageRequest = PageRequest.of(pagingOption.pageNumber, pagingOption.pageSize)

        val (totalCount, list) = matchingService.readManyAndTotalCountWithUsers(loginUser.userId, pageRequest)

        val newList = list.map { matching -> MatchingResponse.fromMatching(matching) }

        return MatchingesResponse(totalCount, newList)
    }
}
