package com.example.carrental_fe.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest (
    val username: String,
    val password: String,
    val email: String,
    val displayName: String
)