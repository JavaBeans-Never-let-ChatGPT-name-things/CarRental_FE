package com.example.carrental_fe.network

import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Contract
import com.example.carrental_fe.model.enums.ReturnCarStatus
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EmployeeApi {
    @POST("api/employee-contract-management/confirm-pickup/{contractId}")
    suspend fun confirmPickup(
        @Path("contractId") contractId: Long,
    ): MessageResponse

    @GET("api/employee-contract-management/get-pending-contracts")
    suspend fun getPendingContracts(): List<Contract>

    @POST("api/employee-contract-management/reject-assignment/{contractId}")
    suspend fun rejectAssignment(
        @Path("contractId") contractId: Long,
    ): MessageResponse

    @POST("api/employee-contract-management/confirm-assignment/{contractId}")
    suspend fun confirmAssignment(
        @Path("contractId") contractId: Long,
    ): MessageResponse
    @POST("api/employee-contract-management/confirm-return/{contractId}")
    suspend fun confirmReturn(
        @Path("contractId") contractId: Long,
        @Body returnCarStatus: ReturnCarStatus
    ): MessageResponse

    @GET("api/employee-contract-management/get-employee-contracts")
    suspend fun getEmployeeContracts(): List<Contract>
}