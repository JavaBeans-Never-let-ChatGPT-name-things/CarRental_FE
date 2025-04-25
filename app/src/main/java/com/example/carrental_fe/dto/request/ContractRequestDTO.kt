package com.example.carrental_fe.dto.request

import com.example.carrental_fe.model.enums.PaymentStatus
import com.example.carrental_fe.model.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ContractRequestDTO(
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate,
    val deposit: Float,
    val paymentMethod: String,
    val paymentStatus: PaymentStatus,
    val totalPrice: Float
)
