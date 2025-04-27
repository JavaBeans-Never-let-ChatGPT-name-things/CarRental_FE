package com.example.carrental_fe.data

interface NotificationRepository {
    suspend fun registerToken(fcmToken: String): String
}