package com.example.carrental_fe.dto.response

import com.example.carrental_fe.model.enums.ReturnCarStatus
import kotlinx.serialization.Serializable

@Serializable
data class ReturnStatusDTO (
    val returnCarStatus: ReturnCarStatus,
    val value: Int
)