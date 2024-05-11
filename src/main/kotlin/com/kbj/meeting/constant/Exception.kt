package com.kbj.meeting.constant

import org.springframework.http.HttpStatus

// 발생 가능한 Exception
enum class ExceptionEnum(val status: Int, val message: String) {
    BadParameterRequestException(HttpStatus.BAD_REQUEST.value(), "BadParameterRequest"), // 400
    BadRequestException(HttpStatus.BAD_REQUEST.value(), "BadRequest"), // 400
    InvalidTokenException(HttpStatus.UNAUTHORIZED.value(), "InvalidToken"), // 401
    ExpiredTokenException(HttpStatus.UNAUTHORIZED.value(), "ExpiredToken"), // 401
    ForbiddenException(HttpStatus.FORBIDDEN.value(), "Forbidden"), // 403
    NotFoundException(HttpStatus.NOT_FOUND.value(), "NotFound"), // 404
    ConflictException(HttpStatus.CONFLICT.value(), "Conflict"), // 409
    InternalServerException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "InternalServer"), // 500
}

// Exception 기본 형태
open class CustomException(
    info: ExceptionEnum,
    message: String? = null,
    val data: Map<String, Any>? = null,
) :
    Exception(message ?: info.message) {
    val status: Int = info.status
    val type: String = info.name
}

class BadParameterRequestException(message: String? = null, data: Map<String, Any>? = null) :
    CustomException(ExceptionEnum.BadParameterRequestException, message, data)

class InvalidTokenException(message: String? = null, data: Map<String, Any>? = null) :
    CustomException(ExceptionEnum.InvalidTokenException, message, data)

class ExpiredTokenException(message: String? = null, data: Map<String, Any>? = null) :
    CustomException(ExceptionEnum.ExpiredTokenException, message, data)

class ForbiddenException(message: String? = null, data: Map<String, Any>? = null) :
    CustomException(ExceptionEnum.ForbiddenException, message, data)

class BadRequestException(message: String? = null, data: Map<String, Any>? = null) :
    CustomException(ExceptionEnum.BadRequestException, message, data)

class NotFoundException(message: String? = null, data: Map<String, Any>? = null) :
    CustomException(ExceptionEnum.NotFoundException, message, data)

class ConflictException(message: String? = null, data: Map<String, Any>? = null) :
    CustomException(ExceptionEnum.ConflictException, message, data)

class InternalServerException(message: String? = null, data: Map<String, Any>? = null) :
    CustomException(ExceptionEnum.InternalServerException, message, data)
