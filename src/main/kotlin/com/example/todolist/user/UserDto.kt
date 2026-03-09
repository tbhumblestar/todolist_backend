package com.example.todolist.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val provider: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(user: User): UserResponse = UserResponse(
            id = user.id,
            email = user.email,
            name = user.name,
            provider = user.provider,
            createdAt = user.createdAt
        )
    }
}

data class UserUpdateRequest(
    @field:NotBlank @field:Size(max = 100)
    val name: String
)
