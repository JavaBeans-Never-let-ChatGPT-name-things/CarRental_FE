package com.example.carrental_fe.data

import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Contract
import com.example.carrental_fe.model.enums.ReturnCarStatus

interface EmployeeRepository {
    suspend fun confirmPickup(
        contractId: Long,
    ): MessageResponse
    suspend fun getPendingContracts(): List<Contract>
    suspend fun rejectAssignment(
        contractId: Long,
    ): MessageResponse
    suspend fun confirmAssignment(
        contractId: Long,
    ): MessageResponse
    suspend fun confirmReturn(
        contractId: Long,
        returnCarStatus: ReturnCarStatus
    ): MessageResponse
}