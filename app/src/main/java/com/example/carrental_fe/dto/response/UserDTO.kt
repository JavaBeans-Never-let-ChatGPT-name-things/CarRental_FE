package com.example.carrental_fe.dto.response

import com.example.carrental_fe.model.enums.AccountRole
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO (
    val displayName: String,
    val email: String,
    val phoneNumber: String?,
    val avatarUrl: String?,
    val role: AccountRole,
    val enabled: Boolean,
    val gender: Int,
    val countractCount: Long
)