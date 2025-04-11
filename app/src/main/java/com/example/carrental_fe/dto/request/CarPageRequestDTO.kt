package com.example.carrental_fe.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CarPageRequestDTO (
    val pageNo: Int,
    val pageSize: Int = 10,
    val sort: String,
    val sortByColumn: String
)