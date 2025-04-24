package com.example.carrental_fe.data

import com.example.carrental_fe.dto.request.ItemRequest
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.dto.response.PaymentResponse
import com.example.carrental_fe.network.PayOsApi

class PayOsRepositoryImpl(private val payOsApi: PayOsApi) : PayOsRepository {
    override suspend fun getCheckoutUrl(itemRequest: ItemRequest): PaymentResponse {
        return payOsApi.getCheckoutUrl(itemRequest)
    }

    override suspend fun paymentSuccess(orderId: Long): MessageResponse {
        return payOsApi.paymentSuccess(orderId)
    }

    override suspend fun paymentFailed(carId: String): MessageResponse {
        return payOsApi.paymentFailed(carId)
    }
}