package com.example.carrental_fe.data

import com.example.carrental_fe.dto.request.ContractRequestDTO
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Account
import retrofit2.Response
import java.io.File

interface AccountRepository {
    suspend fun getAccount(): Response<Account>
    suspend fun updateProfile(
        email: String,
        displayName: String,
        gender: Int,
        address: String,
        phoneNumber: String,
        avatarFile: File? = null
    ) : MessageResponse
    suspend fun rentCar(
        carId: String,
        contractRequestDTO: ContractRequestDTO
    ) : Response<Long>
}