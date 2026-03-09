package com.example.todolist.todo

import com.example.todolist.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/todos")
class TodoController(
    private val todoService: TodoService
) {
    @GetMapping
    fun getTodos(authentication: Authentication): ResponseEntity<ApiResponse<List<TodoListResponse>>> {
        val userId = authentication.principal as Long
        return ResponseEntity.ok(ApiResponse.success(todoService.getTodos(userId)))
    }

    @GetMapping("/completed")
    fun getCompletedTodos(
        authentication: Authentication,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<ApiResponse<List<TodoListResponse>>> {
        val userId = authentication.principal as Long
        return ResponseEntity.ok(ApiResponse.success(todoService.getCompletedTodosByDate(userId, date)))
    }

    @GetMapping("/{id}")
    fun getTodo(
        authentication: Authentication,
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<TodoResponse>> {
        val userId = authentication.principal as Long
        return ResponseEntity.ok(ApiResponse.success(todoService.getTodo(userId, id)))
    }

    @PostMapping
    fun createTodo(
        authentication: Authentication,
        @Valid @RequestBody request: TodoCreateRequest
    ): ResponseEntity<ApiResponse<TodoResponse>> {
        val userId = authentication.principal as Long
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(todoService.createTodo(userId, request)))
    }

    @PutMapping("/{id}")
    fun updateTodo(
        authentication: Authentication,
        @PathVariable id: Long,
        @Valid @RequestBody request: TodoUpdateRequest
    ): ResponseEntity<ApiResponse<TodoResponse>> {
        val userId = authentication.principal as Long
        return ResponseEntity.ok(ApiResponse.success(todoService.updateTodo(userId, id, request)))
    }

    @PatchMapping("/{id}/toggle")
    fun toggleTodo(
        authentication: Authentication,
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<TodoResponse>> {
        val userId = authentication.principal as Long
        return ResponseEntity.ok(ApiResponse.success(todoService.toggleTodo(userId, id)))
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(
        authentication: Authentication,
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<Nothing?>> {
        val userId = authentication.principal as Long
        todoService.deleteTodo(userId, id)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    // --- Attachments ---

    @GetMapping("/{todoId}/attachments")
    fun getAttachments(
        authentication: Authentication,
        @PathVariable todoId: Long
    ): ResponseEntity<ApiResponse<List<TodoAttachmentResponse>>> {
        val userId = authentication.principal as Long
        return ResponseEntity.ok(ApiResponse.success(todoService.getAttachments(userId, todoId)))
    }

    @PostMapping("/{todoId}/attachments")
    fun addAttachment(
        authentication: Authentication,
        @PathVariable todoId: Long,
        @RequestBody request: TodoAttachmentCreateRequest
    ): ResponseEntity<ApiResponse<TodoAttachmentResponse>> {
        val userId = authentication.principal as Long
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(todoService.addAttachment(userId, todoId, request)))
    }

    @DeleteMapping("/{todoId}/attachments/{attachmentId}")
    fun deleteAttachment(
        authentication: Authentication,
        @PathVariable todoId: Long,
        @PathVariable attachmentId: Long
    ): ResponseEntity<ApiResponse<Nothing?>> {
        val userId = authentication.principal as Long
        todoService.deleteAttachment(userId, todoId, attachmentId)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}
