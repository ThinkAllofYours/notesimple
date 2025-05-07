package com.example.notesimple.exception.handler

import com.example.notesimple.exception.NoteNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice // 전역 예외 처리기
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(NoteNotFoundException::class)
    fun handleNoteNotFoundException(e: NoteNotFoundException): ResponseEntity<ErrorResponse> {
        log.warn("Note not found: ${e.message}")
        val errorResponse = ErrorResponse(HttpStatus.NOT_FOUND.value(), e.message ?: "리소스를 찾을 수 없습니다.")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class) // Validation 예외 처리
    fun handleValidationExceptions(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = e.bindingResult.fieldErrors.map { "${it.field}: ${it.defaultMessage}" }
        log.warn("Validation failed: $errors")
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), "입력값이 유효하지 않습니다.", errors)
        return ResponseEntity.badRequest().body(errorResponse)
    }

    @ExceptionHandler(Exception::class) // 그 외 모든 예외 처리
    fun handleGlobalException(e: Exception): ResponseEntity<ErrorResponse> {
        log.error("Unhandled exception occurred", e)
        val errorResponse = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다.")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}

// 공통 에러 응답 DTO
data class ErrorResponse(
    val status: Int,
    val message: String,
    val errors: List<String>? = null // Validation 에러 상세 내용 등
)