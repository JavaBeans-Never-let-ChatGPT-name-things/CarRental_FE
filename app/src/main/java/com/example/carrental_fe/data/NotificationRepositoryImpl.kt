package com.example.carrental_fe.data

import com.example.carrental_fe.network.NotificationApi
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Notification

class NotificationRepositoryImpl (private val notificationApi: NotificationApi): NotificationRepository{
    override suspend fun registerToken(fcmToken: String): String
            = notificationApi.registerToken(fcmToken)

    override suspend fun getNotifications(): List<Notification>
            = notificationApi.getNotifications()

    override suspend fun readAllNotifications(): MessageResponse
            = notificationApi.readAllNotifications()

    override suspend fun deleteAllNotifications(): MessageResponse
    = notificationApi.deleteAllNotifications()
}