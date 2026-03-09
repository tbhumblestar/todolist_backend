package com.example.todolist.todo

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface TodoRepository : JpaRepository<Todo, Long> {
    fun findByIsDeletedFalseAndUserId(userId: Long): List<Todo>
    fun findByIdAndIsDeletedFalse(id: Long): Todo?
    fun findByIsDeletedFalseAndUserIdAndCompletedTrueAndUpdatedAtBetween(
        userId: Long,
        start: LocalDateTime,
        end: LocalDateTime
    ): List<Todo>
}
