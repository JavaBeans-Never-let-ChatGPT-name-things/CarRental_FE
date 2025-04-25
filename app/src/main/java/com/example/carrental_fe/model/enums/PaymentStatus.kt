package com.example.carrental_fe.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class PaymentStatus {
    SUCCESS, FAILED
}