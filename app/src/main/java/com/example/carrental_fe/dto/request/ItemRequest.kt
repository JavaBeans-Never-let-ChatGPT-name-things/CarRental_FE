package com.example.carrental_fe.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ItemRequest (
    val name: String,
    val quantity: Int,
    val price: Double
)