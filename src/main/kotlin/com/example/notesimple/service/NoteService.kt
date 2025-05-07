package com.example.notesimple.service

import com.example.notesimple.controller.dto.CreateNoteRequest
import com.example.notesimple.controller.dto.NoteResponse
import com.example.notesimple.controller.dto.UpdateNoteRequest
import com.example.notesimple.domain.Note
import com.example.notesimple.domain.search.NoteDocument
import com.example.notesimple.repository.NoteRepository
import com.example.notesimple.repository.search.NoteSearchRepository
import com.example.notesimple.exception.NoteNotFoundException
import org.springframework.data.repository.findByIdOrNull // Kotlin 확장 함수 활용
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional // 트랜잭션 관리
import jakarta.persistence.EntityNotFoundException

@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션, CUD 메소드에 별도 설정
class NoteService(
    private val noteRepository: NoteRepository,
    private val noteSearchRepository: NoteSearchRepository
) {
    fun getNoteById(id: Long): NoteResponse {
        val note = findNoteEntityById(id)
        return NoteResponse.fromEntity(note)
    }

    fun getAllNotes(): List<NoteResponse> {
        return noteRepository.findAll().map { NoteResponse.fromEntity(it) }
    }

    @Transactional // 쓰기 트랜잭션
    fun createNote(request: CreateNoteRequest): NoteResponse {
        val note = Note(
            title = request.title,
            content = request.content
            // author = request.author // Phase 2
        )
        val savedNote = noteRepository.save(note)
        // ES 색인
        noteSearchRepository.save(NoteDocument.fromEntity(savedNote))
        return NoteResponse.fromEntity(savedNote)
    }

    @Transactional
    fun updateNote(id: Long, request: UpdateNoteRequest): NoteResponse {
        val updatedNote = findNoteEntityById(id)
        updatedNote.title = request.title
        updatedNote.content = request.content
        // ES 업데이트
        noteSearchRepository.save(NoteDocument.fromEntity(updatedNote))
        return NoteResponse.fromEntity(updatedNote) // 업데이트된 엔티티 반환
    }

    @Transactional
    fun deleteNote(id: Long) {
        val note = findNoteEntityById(id) // 삭제 전 존재 확인
        noteRepository.delete(note)
        noteSearchRepository.deleteById(id.toString())
    }

    fun searchNotesByTag(tag: String): List<NoteResponse> {
        return noteSearchRepository.findByTags(tag).map(NoteResponse::fromDocument);
    }

    fun searchNotesByTitleOrContent(query: String): List<NoteResponse> {
        return noteSearchRepository.findByTitleContainingOrContentContaining(query, query).map { NoteResponse.fromDocument(it) }
    }

    // 내부 헬퍼 메소드 (ID로 엔티티 조회, 없을 시 예외 발생)
    private fun findNoteEntityById(id: Long): Note {
        return noteRepository.findByIdOrNull(id) ?: throw NoteNotFoundException("노트를 찾을 수 없습니다: $id")
    }
}