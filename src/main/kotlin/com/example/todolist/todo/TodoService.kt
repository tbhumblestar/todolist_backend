package com.example.todolist.todo

import com.example.todolist.common.exception.CustomException
import com.example.todolist.common.exception.ErrorCode
import com.example.todolist.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class TodoService(
    private val todoRepository: TodoRepository,
    private val todoAttachmentRepository: TodoAttachmentRepository,
    private val userRepository: UserRepository
) {
    fun getTodos(userId: Long): List<TodoListResponse> {
        val todos = todoRepository.findByIsDeletedFalseAndUserId(userId)
        return toListResponses(todos)
    }

    fun getCompletedTodosByDate(userId: Long, date: LocalDate): List<TodoListResponse> {
        val start = date.atStartOfDay()
        val end = date.plusDays(1).atStartOfDay()
        val todos = todoRepository.findByIsDeletedFalseAndUserIdAndCompletedTrueAndUpdatedAtBetween(
            userId, start, end
        )
        return toListResponses(todos)
    }

    private fun toListResponses(todos: List<Todo>): List<TodoListResponse> {
        val todoIds = todos.map { it.id }
        if (todoIds.isEmpty()) return emptyList()
        val countMap = todoAttachmentRepository.countByTodoIdIn(todoIds)
            .associate { (todoId, count) -> todoId as Long to (count as Long).toInt() }
        val thumbnailMap = todoAttachmentRepository.findFirstByTodoIdIn(todoIds)
            .associate { it.todo.id to it.fileUrl }
        return todos.map { TodoListResponse.from(it, countMap[it.id] ?: 0, thumbnailMap[it.id]) }
    }

    fun getTodo(userId: Long, id: Long): TodoResponse {
        val todo = findTodoWithOwnership(userId, id)
        val attachments = todoAttachmentRepository.findByTodoId(id)
        return TodoResponse.from(todo, attachments)
    }

    @Transactional
    fun createTodo(userId: Long, request: TodoCreateRequest): TodoResponse {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw CustomException(ErrorCode.USER_NOT_FOUND)
        val todo = Todo(user = user, title = request.title, description = request.description, dueDate = request.dueDate)
        return TodoResponse.from(todoRepository.save(todo))
    }

    @Transactional
    fun updateTodo(userId: Long, id: Long, request: TodoUpdateRequest): TodoResponse {
        val todo = findTodoWithOwnership(userId, id)
        todo.updateTitle(request.title)
        todo.updateDescription(request.description)
        todo.updateDueDate(request.dueDate)
        val attachments = todoAttachmentRepository.findByTodoId(id)
        return TodoResponse.from(todo, attachments)
    }

    @Transactional
    fun toggleTodo(userId: Long, id: Long): TodoResponse {
        val todo = findTodoWithOwnership(userId, id)
        todo.toggleCompleted()
        val attachments = todoAttachmentRepository.findByTodoId(id)
        return TodoResponse.from(todo, attachments)
    }

    @Transactional
    fun deleteTodo(userId: Long, id: Long) {
        val todo = findTodoWithOwnership(userId, id)
        todo.softDelete()
    }

    // --- Attachment ---

    fun getAttachments(userId: Long, todoId: Long): List<TodoAttachmentResponse> {
        findTodoWithOwnership(userId, todoId)
        return todoAttachmentRepository.findByTodoId(todoId).map(TodoAttachmentResponse::from)
    }

    @Transactional
    fun addAttachment(userId: Long, todoId: Long, request: TodoAttachmentCreateRequest): TodoAttachmentResponse {
        val todo = findTodoWithOwnership(userId, todoId)
        val attachment = TodoAttachment(
            todo = todo,
            fileUrl = request.fileUrl,
            fileName = request.fileName,
            fileSize = request.fileSize
        )
        return TodoAttachmentResponse.from(todoAttachmentRepository.save(attachment))
    }

    @Transactional
    fun deleteAttachment(userId: Long, todoId: Long, attachmentId: Long) {
        findTodoWithOwnership(userId, todoId)
        val attachment = todoAttachmentRepository.findById(attachmentId)
            .orElseThrow { CustomException(ErrorCode.ATTACHMENT_NOT_FOUND) }
        if (attachment.todo.id != todoId) {
            throw CustomException(ErrorCode.FORBIDDEN)
        }
        todoAttachmentRepository.delete(attachment)
    }

    private fun findTodoWithOwnership(userId: Long, todoId: Long): Todo {
        val todo = todoRepository.findByIdAndIsDeletedFalse(todoId)
            ?: throw CustomException(ErrorCode.TODO_NOT_FOUND)
        if (todo.user.id != userId) {
            throw CustomException(ErrorCode.FORBIDDEN)
        }
        return todo
    }
}
