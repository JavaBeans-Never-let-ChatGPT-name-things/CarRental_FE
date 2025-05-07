package com.example.carrental_fe.model

import kotlinx.serialization.Serializable

@Serializable
data class Notification (
    val title: String,
    val message: String,
    val isRead: Boolean,
    val imageUrl: String,
)