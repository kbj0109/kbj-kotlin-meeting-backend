package com.kbj.meeting.handler

import com.kbj.meeting.constant.CustomException
import com.kbj.meeting.constant.ExceptionEnum
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestControllerAdvice
class ExceptionHandler {
    var defaultException = ExceptionEnum.InternalServerException

    data class ExceptionResponse(
        val status: Int = ExceptionEnum.InternalServerException.status,
        val type: String = ExceptionEnum.InternalServerException.name,
        val message: String? = null,
        val data: Map<String, Any>? = mutableMapOf(),
        val responseTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        var stack: String? = null,
    )

    // 유효성 검증 실패시 Exception Handling
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBadParameterException(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionResponse> {
        val badParamException = ExceptionEnum.BadParameterRequestException

        val badParamList =
            ((ex.bindingResult.allErrors) as List<FieldError>).map { it.field }

        val exceptionResponse =
            ExceptionResponse(
                status = badParamException.status,
                type = badParamException.name,
                message = badParamException.message,
                data =
                    mapOf(
                        "badParamList" to badParamList,
                    ),
                stack = ex.stackTrace.joinToString("\n") { it.toString() },
            )

        return ResponseEntity(
            exceptionResponse,
            HttpStatusCode.valueOf(exceptionResponse.status),
        )
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(ex: Exception): ResponseEntity<ExceptionResponse> {
        lateinit var exceptionResponse: ExceptionResponse

        if (ex is CustomException) {
            exceptionResponse =
                ExceptionResponse(
                    status = ex.status,
                    type = ex.type,
                    message = ex.message,
                    data = ex.data,
                    responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                )
        } else {
            exceptionResponse =
                ExceptionResponse(
                    status = defaultException.status,
                    type = defaultException.name,
                    message = defaultException.message,
                    data = null,
                    responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                )
        }

        val stackTrace = ex.stackTrace.joinToString("\n") { it.toString() }

        // 개발 환경 한정 설정 필요
        exceptionResponse.stack = stackTrace

        return ResponseEntity(
            exceptionResponse,
            HttpStatusCode.valueOf(exceptionResponse.status),
        )
    }
}
