package com.example.carrental_fe.data

import com.example.carrental_fe.network.NotificationApi

class NotificationRepositoryImpl (private val notificationApi: NotificationApi): NotificationRepository{
    override suspend fun registerToken(fcmToken: String): String
            = notificationApi.registerToken(fcmToken)
}