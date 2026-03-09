package com.example.todolist.todo

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "todo_attachments")
class TodoAttachment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    val todo: Todo,

    @Column(name = "file_url", nullable = false, length = 500)
    val fileUrl: String,

    @Column(name = "file_name", nullable = false, length = 255)
    val fileName: String,

    @Column(name = "file_size", nullable = false)
    val fileSize: Long,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
