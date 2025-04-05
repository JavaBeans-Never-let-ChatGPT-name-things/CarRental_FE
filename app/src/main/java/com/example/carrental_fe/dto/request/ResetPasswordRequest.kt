package com.example.carrental_fe.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest (
    val email: String,
    val verificationCode: String,
    val newPassword: String)