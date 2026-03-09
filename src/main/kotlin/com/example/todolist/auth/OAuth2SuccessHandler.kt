package com.example.todolist.auth

import com.example.todolist.user.User
import com.example.todolist.user.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as OAuth2User
        val email = oAuth2User.getAttribute<String>("email")!!
        val name = oAuth2User.getAttribute<String>("name") ?: email
        val providerId = oAuth2User.getAttribute<String>("sub")!!

        val user = userRepository.findByEmailAndIsDeletedFalse(email)
            ?: userRepository.save(
                User(
                    email = email,
                    password = null,
                    name = name,
                    provider = "google",
                    providerId = providerId
                )
            )

        val token = jwtProvider.generateToken(user.id, user.email)
        response.sendRedirect("http://localhost:3000?token=$token")
    }
}
