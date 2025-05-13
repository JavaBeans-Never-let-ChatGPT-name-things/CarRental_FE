package com.example.carrental_fe.model

import com.example.carrental_fe.model.enums.ContractStatus
import com.example.carrental_fe.model.enums.PaymentStatus
import com.example.carrental_fe.model.enums.ReturnCarStatus
import com.example.carrental_fe.model.serializer.InstantSerializer
import com.example.carrental_fe.model.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate

@Serializable
data class Contract (
    val id: Long,
    val carId: String,
    val carImageUrl: String,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate,
    @Serializable(with = InstantSerializer::class)
    val contractDate: Instant,
    val contractStatus: ContractStatus,
    val paymentStatus: PaymentStatus,
    val returnCarStatus: ReturnCarStatus?,
    val deposit: Float,
    val totalPrice: Float
)