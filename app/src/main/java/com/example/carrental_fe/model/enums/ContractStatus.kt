package com.example.carrental_fe.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ContractStatus {
    BOOKED, PICKED_UP, EXPIRED, COMPLETE, OVERDUE, REVIEWED
}