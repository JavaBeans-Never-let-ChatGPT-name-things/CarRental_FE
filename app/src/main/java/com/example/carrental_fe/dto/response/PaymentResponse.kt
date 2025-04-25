package com.example.carrental_fe.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse (
    val checkoutUrl: String
)
