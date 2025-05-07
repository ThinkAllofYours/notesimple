package com.example.notesimple.repository.search

import com.example.notesimple.domain.search.NoteDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteSearchRepository : ElasticsearchRepository<NoteDocument, String> {
    fun findByTags(tag: String): List<NoteDocument>
    fun findByTitleContainingOrContentContaining(title: String, content: String): List<NoteDocument>
}