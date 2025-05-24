package com.example.carrental_fe.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class UserSummaryDTO (
    val username: String,
    val email: String,
    val displayName: String,
    val gender: Int,
    val phoneNumber: String?,
    val avatarUrl: String?,
    val creditPoint: Double
)