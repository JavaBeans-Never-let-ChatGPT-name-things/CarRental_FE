package com.example.carrental_fe.data

import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Notification

interface NotificationRepository {
    suspend fun registerToken(fcmToken: String): String
    suspend fun getNotifications() : List<Notification>
    suspend fun  readAllNotifications(): MessageResponse
    suspend fun deleteAllNotifications(): MessageResponse
}