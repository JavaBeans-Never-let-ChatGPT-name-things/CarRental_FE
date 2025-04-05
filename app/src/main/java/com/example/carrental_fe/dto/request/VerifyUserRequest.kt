package com.example.carrental_fe.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class VerifyUserRequest (val email: String, val verificationCode: String) {
}