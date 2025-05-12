package com.example.carrental_fe.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ReturnCarStatus {
    INTACT, NOT_RETURNED, DAMAGED, LOST
}