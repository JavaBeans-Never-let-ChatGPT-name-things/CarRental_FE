package com.example.carrental_fe.network

import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.dto.response.UserDTO
import com.example.carrental_fe.dto.response.UserDetailDTO
import com.example.carrental_fe.model.Contract
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @GET("api/user-management/employees/available/{contractId}")
    suspend fun getAvailableEmployees(
        @Path("contractId") contractId: Long
    ): List<String>

    @Multipart
    @POST("api/admin/cars/add/carBrand")
    suspend fun addCarBrand(
        @Part("name") name: RequestBody,
        @Part logo: MultipartBody.Part? = null
    ): MessageResponse

    @Multipart
    @POST("api/admin/cars/add")
    suspend fun addCar(
        @Part("id") id: RequestBody,
        @Part("brandName") brandName: RequestBody,
        @Part("maxSpeed") maxSpeed: RequestBody,
        @Part("carRange") carRange: RequestBody,
        @Part carImage: MultipartBody.Part? = null,
        @Part("seatsNumber") seatsNumber: RequestBody,
        @Part("rentalPrice") rentalPrice: RequestBody,
        @Part("engineType") engineType: RequestBody,
        @Part("gearType") gearType: RequestBody,
        @Part("drive") drive: RequestBody,
    ): MessageResponse

}