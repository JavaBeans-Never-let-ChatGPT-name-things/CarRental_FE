package com.example.carrental_fe.network

import com.example.carrental_fe.dto.request.ItemRequest
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.dto.response.PaymentResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PayOsApi {
    @POST("/api/payment/create")
    suspend fun getCheckoutUrl(@Body itemRequest: ItemRequest): PaymentResponse
    @POST("/api/payment/success/{orderId}")
    suspend fun paymentSuccess(@Path(value="orderId") orderId: Long): MessageResponse
    @POST("/api/payment/failed/{carId}")
    suspend fun paymentFailed(@Path(value="carId") carId: String): MessageResponse
}