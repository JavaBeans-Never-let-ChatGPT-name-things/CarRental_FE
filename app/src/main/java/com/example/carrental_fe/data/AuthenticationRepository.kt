package com.example.carrental_fe.data

import com.example.carrental_fe.dto.request.EmailRequest
import com.example.carrental_fe.dto.request.LoginRequest
import com.example.carrental_fe.dto.request.RegisterRequest
import com.example.carrental_fe.dto.request.ResetPasswordRequest
import com.example.carrental_fe.dto.response.TokenResponse
import com.example.carrental_fe.dto.request.VerifyUserRequest
import com.example.carrental_fe.dto.response.MessageResponse

interface AuthenticationRepository{
    suspend fun login(loginRequest: LoginRequest) : TokenResponse
    suspend fun register(registerRequest: RegisterRequest) : MessageResponse
    suspend fun verify(verifyUserRequest: VerifyUserRequest): TokenResponse
    suspend fun resend(emailRequest: EmailRequest) : MessageResponse
    suspend fun forgot(emailRequest: EmailRequest) : MessageResponse
    suspend fun reset(forgotPasswordRequest: ResetPasswordRequest) : MessageResponse
    suspend fun resendForgot(emailRequest: EmailRequest) : MessageResponse
    suspend fun refresh(refreshToken: String) : TokenResponse
    suspend fun logout(refreshToken: String) : MessageResponse
}