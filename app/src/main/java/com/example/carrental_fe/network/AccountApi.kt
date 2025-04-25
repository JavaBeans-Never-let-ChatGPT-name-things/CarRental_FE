package com.example.carrental_fe.network

import com.example.carrental_fe.dto.request.ContractRequestDTO
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Account
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AccountApi {
    @GET("/api/accounts")
    suspend fun getAccount(): Response<Account>

    @Multipart
    @POST("/api/accounts/update/profile")
    suspend fun updateProfile(
        @Part("email") email: RequestBody,
        @Part("displayName") displayName: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("address") address: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part avatar: MultipartBody.Part? = null
    ): MessageResponse
    @POST("/api/accounts/rentCar/{carId}")
    suspend fun rentCar(@Path("carId") carId: String, @Body contractRequestDTO: ContractRequestDTO): Response<Long>
}