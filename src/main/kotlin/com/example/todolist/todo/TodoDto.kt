package com.example.todolist.todo

import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class TodoCreateRequest(
    @field:NotBlank
    val title: String,
    val description: Map<String, Any?>? = null,
    val dueDate: LocalDateTime? = null
)

data class TodoUpdateRequest(
    @field:NotBlank
    val title: String,
    val description: Map<String, Any?>? = null,
    val dueDate: LocalDateTime? = null
)

data class TodoListResponse(
    val id: Long,
    val title: String,
    val completed: Boolean,
    val dueDate: LocalDateTime?,
    val attachmentCount: Int,
    val thumbnailUrl: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(todo: Todo, attachmentCount: Int = 0, thumbnailUrl: String? = null): TodoListResponse = TodoListResponse(
            id = todo.id,
            title = todo.title,
            completed = todo.completed,
            dueDate = todo.dueDate,
            attachmentCount = attachmentCount,
            thumbnailUrl = thumbnailUrl,
            createdAt = todo.createdAt,
            updatedAt = todo.updatedAt
        )
    }
}

data class TodoResponse(
    val id: Long,
    val title: String,
    val description: Map<String, Any?>?,
    val completed: Boolean,
    val dueDate: LocalDateTime?,
    val attachments: List<TodoAttachmentResponse>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(todo: Todo, attachments: List<TodoAttachment> = emptyList()): TodoResponse = TodoResponse(
            id = todo.id,
            title = todo.title,
            description = todo.description,
            completed = todo.completed,
            dueDate = todo.dueDate,
            attachments = attachments.map(TodoAttachmentResponse::from),
            createdAt = todo.createdAt,
            updatedAt = todo.updatedAt
        )
    }
}

data class TodoAttachmentResponse(
    val id: Long,
    val fileUrl: String,
    val fileName: String,
    val fileSize: Long,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(attachment: TodoAttachment): TodoAttachmentResponse = TodoAttachmentResponse(
            id = attachment.id,
            fileUrl = attachment.fileUrl,
            fileName = attachment.fileName,
            fileSize = attachment.fileSize,
            createdAt = attachment.createdAt
        )
    }
}

data class TodoAttachmentCreateRequest(
    val fileUrl: String,
    val fileName: String,
    val fileSize: Long
)
