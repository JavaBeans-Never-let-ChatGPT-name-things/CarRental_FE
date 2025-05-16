package com.example.carrental_fe.data

import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.dto.response.UserDTO
import com.example.carrental_fe.dto.response.UserDetailDTO
import com.example.carrental_fe.model.Contract
import com.example.carrental_fe.network.AdminApi

class AdminRepositoryImpl(private val adminApi: AdminApi) : AdminRepository {
    override suspend fun getUserList(
        query: String,
        sort: String,
        status: String
    ): List<UserDTO>
            = adminApi.getUserList(query, sort, status)

    override suspend fun getUserDetail(displayName: String): UserDetailDTO
            = adminApi.getUserDetail(displayName)

    override suspend fun promoteUser(displayName: String): MessageResponse
            = adminApi.promoteUser(displayName)

    override suspend fun demoteUser(displayName: String): MessageResponse
            = adminApi.demoteUser(displayName)

    override suspend fun getContracts(): List<Contract>
            = adminApi.getContracts()

    override suspend fun assignContract(
        contractId: Long,
        employeeName: String
    ): MessageResponse
            = adminApi.assignContract(contractId, employeeName)
    override suspend fun getAvailableEmployees(contractId: Long): List<String>
            = adminApi.getAvailableEmployees(contractId)
}