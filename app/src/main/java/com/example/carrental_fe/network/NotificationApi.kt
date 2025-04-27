package com.example.carrental_fe.network

import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationApi {
    @POST("api/notifications/register/{fcmToken}")
    suspend fun registerToken(@Path("fcmToken") fcmToken: String): String
}