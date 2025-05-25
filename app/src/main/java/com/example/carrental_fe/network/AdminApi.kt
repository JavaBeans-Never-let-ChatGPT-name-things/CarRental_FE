package com.example.carrental_fe.network

import com.example.carrental_fe.dto.request.ReportRequestDTO
import com.example.carrental_fe.dto.response.UserSummaryDTO
import com.example.carrental_fe.dto.response.CarSummaryDTO
import com.example.carrental_fe.dto.response.ContractSummaryDTO
import com.example.carrental_fe.dto.response.MonthlyReportDTO
import com.example.carrental_fe.dto.response.ReturnStatusDTO
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.dto.response.UserDTO
import com.example.carrental_fe.dto.response.UserDetailDTO
import com.example.carrental_fe.model.Contract
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
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

    @GET("api/user-management/top-3-best-user")
    suspend fun getTop3BestUsers(): List<UserSummaryDTO>

    @GET("api/user-management/top-3-worst-user")
    suspend fun getTop3WorstUsers(): List<UserSummaryDTO>

    @POST("api/user-management/top-3-best-user-from-date-to-date")
    suspend fun getTop3BestUsersFromDateToDate(
        @Body reportRequestDTO: ReportRequestDTO
    ): List<UserSummaryDTO>

    @POST("api/user-management/top-3-worst-user-from-date-to-date")
    suspend fun getTop3WorstUsersFromDateToDate(
        @Body reportRequestDTO: ReportRequestDTO
    ): List<UserSummaryDTO>

    @GET("api/admin/cars/top-3-rented")
    suspend fun getTop3RentedCars(): List<CarSummaryDTO>

    @GET("api/admin/cars/top-3-rating")
    suspend fun getTop3RatingCars(): List<CarSummaryDTO>

    @POST("api/admin/cars/top-3-rented-from-date-to-date")
    suspend fun getTop3RentedCarsFromDateToDate(
        @Body reportRequestDTO: ReportRequestDTO
    ): List<CarSummaryDTO>

    @POST("api/admin/cars/top-3-rating-from-date-to-date")
    suspend fun getTop3RatingCarsFromDateToDate(
        @Body reportRequestDTO: ReportRequestDTO
    ): List<CarSummaryDTO>

    @GET("api/admin-contract-management/total-revenue/{year}")
    suspend fun getMonthlyTotalRevenue(
        @Path("year") year: Int
    ): List<MonthlyReportDTO>

    @GET("api/admin-contract-management/total-penalty/{year}")
    suspend fun getMonthlyTotalPenalty(
        @Path("year") year: Int
    ): List<MonthlyReportDTO>

    @POST("api/admin-contract-management/total-revenue-from-date-to-date")
    suspend fun getTotalRevenueFromDateToDate(
        @Body reportRequestDTO: ReportRequestDTO
    ): Double

    @POST("api/admin-contract-management/total-penalty-from-date-to-date")
    suspend fun getTotalPenaltyFromDateToDate(
        @Body reportRequestDTO: ReportRequestDTO
    ): Double

    @GET("api/admin-contract-management/total-revenue")
    suspend fun getTotalRevenue(): Double

    @GET("api/admin-contract-management/total-penalty")
    suspend fun getTotalPenalty(): Double

    @POST("api/admin-contract-management/contract-summary-from-date-to-date")
    suspend fun getContractSummaryFromDateToDate(
        @Body reportRequestDTO: ReportRequestDTO
    ): List<ContractSummaryDTO>

    @GET("api/admin-contract-management/contract-summary")
    suspend fun getContractSummary(): List<ContractSummaryDTO>

    @GET("api/admin-contract-management/returned-status")
    suspend fun getReturnStatus(): List<ReturnStatusDTO>

    @POST("api/admin-contract-management/returned-status-from-date-to-date")
    suspend fun getReturnStatusFromDateToDate(
        @Body reportRequestDTO: ReportRequestDTO
    ): List<ReturnStatusDTO>

}