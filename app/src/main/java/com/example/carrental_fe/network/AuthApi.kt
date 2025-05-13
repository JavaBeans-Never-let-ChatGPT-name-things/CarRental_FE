package com.example.carrental_fe.network

import com.example.carrental_fe.dto.request.EmailRequest
import com.example.carrental_fe.dto.request.LoginRequest
import com.example.carrental_fe.dto.request.RegisterRequest
import com.example.carrental_fe.dto.request.ResetPasswordRequest
import com.example.carrental_fe.dto.response.TokenResponse
import com.example.carrental_fe.dto.request.VerifyUserRequest
import com.example.carrental_fe.dto.response.MessageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest) : TokenResponse

    @POST("/api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest) : MessageResponse

    @POST("/api/auth/verify")
    suspend fun verify(@Body verifyUserRequest: VerifyUserRequest) : TokenResponse

    @POST("/api/auth/resend")
    suspend fun resend(@Body emailRequest: EmailRequest) : MessageResponse

    @POST("/api/auth/forgot")
    suspend fun forgot(@Body emailRequest: EmailRequest) : MessageResponse

    @POST("/api/auth/reset")
    suspend fun reset(@Body forgotPasswordRequest: ResetPasswordRequest): MessageResponse

    @POST("/api/auth/resendForgot")
    suspend fun resendForgot(@Body emailRequest: EmailRequest) : MessageResponse
    @GET("/api/auth/refresh")
    suspend fun refresh(
        @Header("Authorization") token: String
    ): TokenResponse


    @POST("/api/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): MessageResponse
}