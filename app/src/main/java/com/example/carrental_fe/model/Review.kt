package com.example.carrental_fe.model

import kotlinx.serialization.Serializable

@Serializable
data class Review (
    val starsNum: Int,
    val comment: String,
    val accountDisplayName: String,
    val avatarUrl: String?,
)
