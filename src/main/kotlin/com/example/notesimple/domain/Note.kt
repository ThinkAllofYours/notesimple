package com.example.notesimple.domain

import jakarta.persistence.*
import java.time.LocalDateTime
import jakarta.persistence.ElementCollection
import jakarta.persistence.FetchType

@Entity
@Table(name = "notes")
class Note(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 200)
    var title: String,

    @Lob // 대용량
    @Column(nullable = false)
    var content: String? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "note_tags", joinColumns = [JoinColumn(name = "note_id")])
    @Column(name = "tag")
    var tags: List<String> = emptyList(),

) : BaseEntity()

@MappedSuperclass
abstract class BaseEntity(
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)