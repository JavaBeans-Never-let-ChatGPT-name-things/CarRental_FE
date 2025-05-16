package com.example.carrental_fe.data

import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.dto.response.UserDTO
import com.example.carrental_fe.dto.response.UserDetailDTO
import com.example.carrental_fe.model.Contract

interface AdminRepository {
    suspend fun getUserList(
        query: String,
        sort: String,
        status: String,
    ): List<UserDTO>

    suspend fun getUserDetail(
        displayName: String,
    ): UserDetailDTO

    suspend fun promoteUser(
        displayName: String,
    ): MessageResponse

    suspend fun demoteUser(
        displayName: String,
    ): MessageResponse

    suspend fun getContracts(): List<Contract>

    suspend fun assignContract(
        contractId: Long,
        employeeName: String
    ): MessageResponse

    suspend fun getAvailableEmployees(
        contractId: Long
    ): List<String>
}