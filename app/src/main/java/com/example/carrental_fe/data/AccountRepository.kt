package com.example.carrental_fe.data

import com.example.carrental_fe.dto.request.ContractRequestDTO
import com.example.carrental_fe.dto.request.ReviewRequestDTO
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Account
import com.example.carrental_fe.model.Contract
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
    suspend fun getContracts(): List<Contract>
    suspend fun reviewContract(contractId: Long, review: ReviewRequestDTO): MessageResponse
    suspend fun retrySuccess(contractId: Long): MessageResponse
    suspend fun retryContract(contractId: Long): MessageResponse
    suspend fun reportLost(contractId: Long): MessageResponse
    suspend fun extendContract(contractId: Long, extraDays: Int): MessageResponse
    suspend fun checkStatus(): Response<Boolean>
}