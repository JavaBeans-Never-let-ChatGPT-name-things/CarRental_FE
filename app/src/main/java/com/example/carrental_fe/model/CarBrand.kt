package com.example.carrental_fe.model

import kotlinx.serialization.Serializable

@Serializable
data class CarBrand(
    val id: Long,
    val name: String,
    val logoUrl: String
)