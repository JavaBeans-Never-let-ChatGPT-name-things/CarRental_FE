package com.example.carrental_fe.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MonthlyReportDTO (
    val month: Int,
    val value: Double
)