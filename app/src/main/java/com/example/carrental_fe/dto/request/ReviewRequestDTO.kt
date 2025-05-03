package com.example.carrental_fe.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequestDTO (
    val starsNum: Int,
    val comment: String
)