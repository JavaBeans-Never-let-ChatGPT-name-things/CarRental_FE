package com.example.carrental_fe.data

import com.example.carrental_fe.dto.request.EmailRequest
import com.example.carrental_fe.dto.request.LoginRequest
import com.example.carrental_fe.dto.request.RegisterRequest
import com.example.carrental_fe.dto.request.ResetPasswordRequest
import com.example.carrental_fe.dto.response.TokenResponse
import com.example.carrental_fe.dto.request.VerifyUserRequest
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.network.AuthApi

class AuthenticationRepositoryImpl (private val authApi: AuthApi): AuthenticationRepository {
    override suspend fun login(loginRequest: LoginRequest): TokenResponse =
        authApi.login(loginRequest)

    override suspend fun register(registerRequest: RegisterRequest): MessageResponse
            = authApi.register(registerRequest)

    override suspend fun verify(verifyUserRequest: VerifyUserRequest): TokenResponse
            = authApi.verify(verifyUserRequest)

    override suspend fun resend(emailRequest: EmailRequest): MessageResponse
            = authApi.resend(emailRequest)

    override suspend fun forgot(emailRequest: EmailRequest): MessageResponse
            = authApi.forgot(emailRequest)

    override suspend fun reset(forgotPasswordRequest: ResetPasswordRequest) : MessageResponse
            = authApi.reset(forgotPasswordRequest)

    override suspend fun resendForgot(emailRequest: EmailRequest): MessageResponse
            = authApi.resendForgot(emailRequest)

    override suspend fun refresh(refreshToken: String): TokenResponse
            = authApi.refresh(refreshToken)
}