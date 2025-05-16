package com.example.carrental_fe.data

import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Contract
import com.example.carrental_fe.model.enums.ReturnCarStatus
import com.example.carrental_fe.network.EmployeeApi

class EmployeeRepositoryImpl (private val employeeApi: EmployeeApi) : EmployeeRepository {
    override suspend fun confirmPickup(
        contractId: Long,
    ) = employeeApi.confirmPickup(contractId)

    override suspend fun getPendingContracts(): List<Contract>
            = employeeApi.getPendingContracts()

    override suspend fun rejectAssignment(contractId: Long): MessageResponse
            = employeeApi.rejectAssignment(contractId)

    override suspend fun confirmAssignment(contractId: Long): MessageResponse
            = employeeApi.confirmAssignment(contractId)

    override suspend fun confirmReturn(
        contractId: Long,
        returnCarStatus: ReturnCarStatus
    ): MessageResponse
            = employeeApi.confirmReturn(contractId, returnCarStatus)
}