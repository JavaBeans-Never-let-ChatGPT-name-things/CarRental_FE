package com.example.carrental_fe.dto.response

import com.example.carrental_fe.model.enums.ContractStatus
import kotlinx.serialization.Serializable

@Serializable
data class ContractSummaryDTO (
    val contractStatus: ContractStatus,
    val value: Int
)