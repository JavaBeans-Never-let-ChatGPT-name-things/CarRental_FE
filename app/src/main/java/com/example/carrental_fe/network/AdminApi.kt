package com.example.carrental_fe.network

import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.dto.response.UserDTO
import com.example.carrental_fe.dto.response.UserDetailDTO
import com.example.carrental_fe.model.Contract
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminApi {
    @GET("api/user-management/users")
    suspend fun getUserList(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("status") status: String,
    ): List<UserDTO>

    @GET("api/user-management/users/{displayName}")
    suspend fun getUserDetail(
        @Path("displayName") displayName: String,
    ): UserDetailDTO

    @PUT("api/user-management/users/promote/{displayName}")
    suspend fun promoteUser(
        @Path("displayName") displayName: String,
    ): MessageResponse

    @PUT("api/user-management/users/demote/{displayName}")
    suspend fun demoteUser(
        @Path("displayName") displayName: String,
    ): MessageResponse

    @GET("api/admin-contract-management/pending-contracts")
    suspend fun getContracts() : List<Contract>

    @POST("api/admin-contract-management/assignContract/{contractId}/{employeeName}")
    suspend fun assignContract(
        @Path("contractId") contractId: Long,
        @Path("employeeName") employeeName: String
    ): MessageResponse

    @GET("/api/user-management/employees/available/{contractId}")
    suspend fun getAvailableEmployees(
        @Path("contractId") contractId: Long
    ): List<String>
}