package com.example.carrental_fe.network

import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.DELETE
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Notification

interface NotificationApi {
    @POST("api/notifications/register/{fcmToken}")
    suspend fun registerToken(@Path("fcmToken") fcmToken: String): String
    @GET("api/notifications/get")
    suspend fun getNotifications(): List<Notification>
    @POST("api/notifications/readAll")
    suspend fun readAllNotifications(): MessageResponse
    @DELETE("api/notifications/deleteAll")
    suspend fun deleteAllNotifications(): MessageResponse
}