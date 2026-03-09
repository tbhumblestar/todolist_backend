package com.example.todolist.todo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TodoAttachmentRepository : JpaRepository<TodoAttachment, Long> {
    fun findByTodoId(todoId: Long): List<TodoAttachment>

    @Query("SELECT a.todo.id, COUNT(a) FROM TodoAttachment a WHERE a.todo.id IN :todoIds GROUP BY a.todo.id")
    fun countByTodoIdIn(todoIds: List<Long>): List<Array<Any>>

    @Query("SELECT a FROM TodoAttachment a WHERE a.todo.id IN :todoIds AND a.id IN (SELECT MIN(a2.id) FROM TodoAttachment a2 WHERE a2.todo.id IN :todoIds GROUP BY a2.todo.id)")
    fun findFirstByTodoIdIn(todoIds: List<Long>): List<TodoAttachment>
}
