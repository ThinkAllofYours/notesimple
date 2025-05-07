package com.example.notesimple.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

// 생성 요청 DTO
data class CreateNoteRequest(
    @field:NotBlank(message = "제목은 필수")
    @field:Size(max = 200, message = "제목은 최대 200자")
    var title: String,
    var content: String? = null
)

// 수정 요청 DTO
data class UpdateNoteRequest(
    @field:NotBlank(message = "제목은 필수")
    @field:Size(max = 200, message = "제목은 최대 200자")
    var title: String,
    var content: String? = null
)

// 응답 DTO
data class NoteResponse(
    val id: Long,
    val title: String,
    val content: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(note: com.example.notesimple.domain.Note): NoteResponse {
            return NoteResponse(
                id = note.id!!,
                title = note.title,
                content = note.content,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt
            )
        }
        fun fromDocument(document: com.example.notesimple.domain.search.NoteDocument): NoteResponse {
            return NoteResponse(
                id = document.id.toLong(),
                title = document.title,
                content = document.content,
                createdAt = document.createdAt,
                updatedAt = document.updatedAt
            )
        }
    }
}

