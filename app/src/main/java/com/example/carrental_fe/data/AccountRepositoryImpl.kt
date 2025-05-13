package com.example.carrental_fe.data

import com.example.carrental_fe.dto.request.ContractRequestDTO
import com.example.carrental_fe.dto.request.ReviewRequestDTO
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Account
import com.example.carrental_fe.model.Contract
import com.example.carrental_fe.network.AccountApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class AccountRepositoryImpl (private val accountApi: AccountApi): AccountRepository {
    override suspend fun getAccount(): Response<Account>
            = accountApi.getAccount()

    override suspend fun updateProfile(
        email: String,
        displayName: String,
        gender: Int,
        address: String,
        phoneNumber: String,
        avatarFile: File?
    ): MessageResponse {
        val emailPart = email.toRequestBody("text/plain".toMediaType())
        val displayNamePart = displayName.toRequestBody("text/plain".toMediaType())
        val genderPart = gender.toString().toRequestBody("text/plain".toMediaType())
        val addressPart = address.toRequestBody("text/plain".toMediaType())
        val phoneNumberPart = phoneNumber.toRequestBody("text/plain".toMediaType())

        val avatarPart: MultipartBody.Part? = avatarFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("avatar", it.name, requestFile)
        }

        return accountApi.updateProfile(
            email = emailPart,
            displayName = displayNamePart,
            gender = genderPart,
            address = addressPart,
            phoneNumber = phoneNumberPart,
            avatar = avatarPart
        )
    }

    override suspend fun rentCar(
        carId: String,
        contractRequestDTO: ContractRequestDTO
    ) = accountApi.rentCar(carId, contractRequestDTO)

    override suspend fun getContracts(): List<Contract>
            = accountApi.getContracts()

    override suspend fun reviewContract(
        contractId: Long,
        review: ReviewRequestDTO
    ): MessageResponse =
        accountApi.reviewContract(
            contractId = contractId,
            review = review
        )

    override suspend fun retrySuccess(contractId: Long): MessageResponse
            = accountApi.retrySuccess(contractId)

    override suspend fun retryContract(contractId: Long): MessageResponse
            = accountApi.retry(contractId)

    override suspend fun reportLost(contractId: Long): MessageResponse
            = accountApi.confirmLost(contractId)

    override suspend fun extendContract(
        contractId: Long,
        extraDays: Int
    ): MessageResponse
            = accountApi.extendContract(contractId, extraDays)
}