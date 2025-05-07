package com.example.notesimple.domain.search // 별도 패키지 추천

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.LocalDateTime

@Document(indexName = "notes") // Elasticsearch 인덱스 이름
data class NoteDocument(
    @Id
    val id: String, // Elasticsearch의 ID는 보통 String 사용

    @Field(type = FieldType.Text, analyzer = "standard")
    val title: String,

    @Field(type = FieldType.Text, analyzer = "standard")
    val content: String?,

    @Field(type = FieldType.Keyword) // 정확히 일치하는 검색 또는 집계용 필드
    val tags: List<String>? = emptyList(),

    @Field(type = FieldType.Date)
    val createdAt: LocalDateTime,

    @Field(type = FieldType.Date)
    val updatedAt: LocalDateTime
) {
    companion object {
        // Note Entity -> NoteDocument 변환 함수
        fun fromEntity(note: com.example.notesimple.domain.Note): NoteDocument {
            return NoteDocument(
                id = note.id.toString(), // Long ID를 String으로 변환
                title = note.title,
                content = note.content,
                tags = note.tags,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt
            )
        }
    }
}