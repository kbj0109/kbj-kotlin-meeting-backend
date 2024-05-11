package com.kbj.meeting.handler

import com.kbj.meeting.constant.BaseResponse
import com.kbj.meeting.constant.ResultCode
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    // @Valid 어노테이션이 사용될때, DTO 검증 실패시 예외처리
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun methodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
    ): ResponseEntity<BaseResponse<String>> {
        val error = ex.bindingResult.allErrors[0]
        return ResponseEntity(
            BaseResponse(
                statusCode = ResultCode.BadRequestException.statusCode,
                statusMessage = (error as FieldError).field + ": " + error.defaultMessage,
            ),
            HttpStatus.BAD_REQUEST,
        )
    }

    // / 매개변수 값이 올바르게 처리 되지 않았을때 에러처리
    @ExceptionHandler(IllegalArgumentException::class)
    protected fun illegalArgumentException(ex: IllegalArgumentException): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity(
            BaseResponse(statusCode = ResultCode.BadRequestException.statusCode, statusMessage = ex.message),
            HttpStatus.BAD_REQUEST,
        )
    }

    // 기본적인 에러 처리
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(ex: Exception): ResponseEntity<BaseResponse<String>> {
        val resultCode = ResultCode.NotFoundException

        return ResponseEntity(
            BaseResponse(resultCode.statusCode, resultCode.message),
            HttpStatusCode.valueOf(resultCode.statusCode),
        )
    }
}
