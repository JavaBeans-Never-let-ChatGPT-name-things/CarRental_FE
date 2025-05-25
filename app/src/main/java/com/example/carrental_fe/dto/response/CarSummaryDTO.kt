package com.example.carrental_fe.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class CarSummaryDTO (
    val id: String,
    val imageUrl: String,
    val rentalCount: Long,
    val rating: Double
)