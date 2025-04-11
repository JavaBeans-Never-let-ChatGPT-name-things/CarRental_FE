package com.example.carrental_fe.model

import com.example.carrental_fe.model.enums.CarState
import kotlinx.serialization.Serializable

@Serializable
data class Car (
    val id: String,
    val brand: CarBrand,
    val maxSpeed: Float,
    val carRange: Float,
    val carImageUrl: String,
    val state: CarState,
    val seatsNumber: Int,
    val rentalPrice: Float,
    val engineType: String,
    val rating: Float
)