package com.example.todolist.user

import com.example.todolist.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getMe(authentication: Authentication): ResponseEntity<ApiResponse<UserResponse>> {
        val userId = authentication.principal as Long
        return ResponseEntity.ok(ApiResponse.success(userService.getMe(userId)))
    }

    @PutMapping("/me")
    fun updateMe(
        authentication: Authentication,
        @Valid @RequestBody request: UserUpdateRequest
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val userId = authentication.principal as Long
        return ResponseEntity.ok(ApiResponse.success(userService.updateMe(userId, request)))
    }

    @DeleteMapping("/me")
    fun deleteMe(authentication: Authentication): ResponseEntity<ApiResponse<Nothing?>> {
        val userId = authentication.principal as Long
        userService.deleteMe(userId)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}
