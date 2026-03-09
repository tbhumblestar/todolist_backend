package com.example.todolist.user

import com.example.todolist.common.exception.CustomException
import com.example.todolist.common.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {
    fun getMe(userId: Long): UserResponse {
        val user = findUserOrThrow(userId)
        return UserResponse.from(user)
    }

    @Transactional
    fun updateMe(userId: Long, request: UserUpdateRequest): UserResponse {
        val user = findUserOrThrow(userId)
        user.updateName(request.name)
        return UserResponse.from(user)
    }

    @Transactional
    fun deleteMe(userId: Long) {
        val user = findUserOrThrow(userId)
        user.softDelete()
    }

    private fun findUserOrThrow(userId: Long): User =
        userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw CustomException(ErrorCode.USER_NOT_FOUND)
}
