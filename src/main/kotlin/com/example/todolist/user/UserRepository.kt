package com.example.todolist.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByIdAndIsDeletedFalse(id: Long): User?
    fun findByEmailAndIsDeletedFalse(email: String): User?
}
