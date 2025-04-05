package com.example.carrental_fe.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest ( val username: String, val password: String)