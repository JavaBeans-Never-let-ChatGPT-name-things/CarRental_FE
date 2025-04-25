package com.example.carrental_fe.data

import com.example.carrental_fe.dto.request.ItemRequest
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.dto.response.PaymentResponse

interface PayOsRepository {
    suspend fun getCheckoutUrl(itemRequest: ItemRequest): PaymentResponse
    suspend fun paymentSuccess(orderId: Long): MessageResponse
    suspend fun paymentFailed(carId: String): MessageResponse
}