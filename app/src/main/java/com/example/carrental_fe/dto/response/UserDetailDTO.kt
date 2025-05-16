package com.example.carrental_fe.dto.response

import com.example.carrental_fe.model.Contract
import com.example.carrental_fe.model.enums.AccountRole
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailDTO (
    val avatarUrl: String?,
    val displayName: String,
    val username: String,
    val email: String,
    val role: AccountRole,
    val phoneNumber: String?,
    val address: String?,
    val totalPenalty: Float,
    val gender: Int,
    val rentalContracts: List<Contract>
)