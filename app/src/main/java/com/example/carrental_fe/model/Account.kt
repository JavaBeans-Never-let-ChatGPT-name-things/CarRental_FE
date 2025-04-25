package com.example.carrental_fe.model

import kotlinx.serialization.Serializable

@Serializable
data class Account (
    val username: String,
    val email: String,
    val displayName: String,
    val gender: Int,
    val address: String?,
    val phoneNumber: String?,
    val avatarUrl: String?,
)