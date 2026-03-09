package com.example.todolist.auth

import com.example.todolist.common.exception.CustomException
import com.example.todolist.common.exception.ErrorCode
import com.example.todolist.user.User
import com.example.todolist.user.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider
) {
    @Transactional
    fun signup(request: SignupRequest): TokenResponse {
        if (userRepository.findByEmailAndIsDeletedFalse(request.email) != null) {
            throw CustomException(ErrorCode.DUPLICATE_EMAIL)
        }

        val user = userRepository.save(
            User(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                name = request.name,
                provider = "local",
                providerId = null
            )
        )

        return TokenResponse(jwtProvider.generateToken(user.id, user.email))
    }

    @Transactional(readOnly = true)
    fun login(request: LoginRequest): TokenResponse {
        val user = userRepository.findByEmailAndIsDeletedFalse(request.email)
            ?: throw CustomException(ErrorCode.INVALID_CREDENTIALS)

        if (user.password == null || !passwordEncoder.matches(request.password, user.password)) {
            throw CustomException(ErrorCode.INVALID_CREDENTIALS)
        }

        return TokenResponse(jwtProvider.generateToken(user.id, user.email))
    }
}
