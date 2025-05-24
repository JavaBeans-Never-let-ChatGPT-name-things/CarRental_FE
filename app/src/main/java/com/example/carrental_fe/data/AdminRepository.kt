package com.example.carrental_fe.data

import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.dto.response.UserDTO
import com.example.carrental_fe.dto.response.UserDetailDTO
import com.example.carrental_fe.model.Contract
import java.io.File

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

    suspend fun addCarBrand(
        name: String,
        logo: File?
    ): MessageResponse

    suspend fun addCar(
        id: String,
        brandName: String,
        maxSpeed: Float,
        carRange: Float,
        carImage: File?,
        seatsNumber: Int,
        rentalPrice: Float,
        engineType: String,
        gearType: String,
        drive: String,
    ): MessageResponse
}