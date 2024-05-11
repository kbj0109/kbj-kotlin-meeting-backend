package com.kbj.meeting.constant

import org.springframework.http.HttpStatus

enum class ResultCode(val statusCode: Int, val message: String) {
    Success(HttpStatus.OK.value(), "success"), // 200

    BadParameterException(HttpStatus.BAD_REQUEST.value(), "BadParameter"), // 400
    BadRequestException(HttpStatus.BAD_REQUEST.value(), "BadRequest"), // 400
    InvalidTokenException(HttpStatus.UNAUTHORIZED.value(), "InvalidToken"), // 401
    ExpiredTokenException(HttpStatus.UNAUTHORIZED.value(), "ExpiredToken"), // 401
    ForbiddenException(HttpStatus.FORBIDDEN.value(), "Forbidden"), // 403
    NotFoundException(HttpStatus.NOT_FOUND.value(), "NotFound"), // 404
    ConflictException(HttpStatus.CONFLICT.value(), "Conflict"), // 409
    InternalServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "InternalServer"), // 500
}
