package com.example.carrental_fe.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val refreshToken: String,
    val accessToken: String,
    val role: String
)