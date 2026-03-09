package com.example.todolist.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignupRequest(
    @field:NotBlank @field:Email
    val email: String,

    @field:NotBlank @field:Size(min = 8, max = 100)
    val password: String,

    @field:NotBlank @field:Size(max = 100)
    val name: String
)

data class LoginRequest(
    @field:NotBlank @field:Email
    val email: String,

    @field:NotBlank
    val password: String
)

data class TokenResponse(
    val token: String
)
