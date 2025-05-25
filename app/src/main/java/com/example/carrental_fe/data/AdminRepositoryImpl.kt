package com.example.carrental_fe.data

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
import com.example.carrental_fe.network.AdminApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

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


    override suspend fun addCarBrand(
        name: String,
        logo: File?
    ): MessageResponse {
        val namePart = name.toRequestBody("text/plain".toMediaType())
        val logoPart: MultipartBody.Part? = logo?.let {
            val requestFile = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("logo", it.name, requestFile)
        }
        return adminApi.addCarBrand(
            name = namePart,
            logo = logoPart
        )
    }

    override suspend fun addCar(
        id: String,
        brandName: String,
        maxSpeed: Float,
        carRange: Float,
        carImage: File?,
        seatsNumber: Int,
        rentalPrice: Float,
        engineType: String,
        gearType: String,
        drive: String
    ): MessageResponse {
        val idPart = id.toRequestBody("text/plain".toMediaType())
        val brandPart = brandName.toRequestBody("text/plain".toMediaType())
        val maxSpeedPart = maxSpeed.toString().toRequestBody("text/plain".toMediaType())
        val carRangePart = carRange.toString().toRequestBody("text/plain".toMediaType())
        val seatsNumberPart = seatsNumber.toString().toRequestBody("text/plain".toMediaType())
        val rentalPricePart = rentalPrice.toString().toRequestBody("text/plain".toMediaType())
        val engineTypePart = engineType.toRequestBody("text/plain".toMediaType())
        val gearTypePart = gearType.toRequestBody("text/plain".toMediaType())
        val drivePart = drive.toRequestBody("text/plain".toMediaType())
        val carImagePart: MultipartBody.Part? = carImage?.let {
            val requestFile = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("carImage", it.name, requestFile)
        }
        return adminApi.addCar(
            id = idPart,
            brandName = brandPart,
            maxSpeed = maxSpeedPart,
            carRange = carRangePart,
            carImage = carImagePart,
            seatsNumber = seatsNumberPart,
            rentalPrice = rentalPricePart,
            engineType = engineTypePart,
            gearType = gearTypePart,
            drive = drivePart
        )
    }

    override suspend fun getTop3BestUsers(): List<UserSummaryDTO>
            = adminApi.getTop3BestUsers()

    override suspend fun getTop3WorstUsers(): List<UserSummaryDTO>
            = adminApi.getTop3WorstUsers()

    override suspend fun getTop3BestUsersFromDateToDate(reportRequestDTO: ReportRequestDTO): List<UserSummaryDTO>
            = adminApi.getTop3BestUsersFromDateToDate(reportRequestDTO)

    override suspend fun getTop3WorstUsersFromDateToDate(reportRequestDTO: ReportRequestDTO): List<UserSummaryDTO>
            = adminApi.getTop3WorstUsersFromDateToDate(reportRequestDTO)

    override suspend fun getTop3RentedCars(): List<CarSummaryDTO>
            = adminApi.getTop3RentedCars()

    override suspend fun getTop3RentedCarsFromDateToDate(reportRequestDTO: ReportRequestDTO): List<CarSummaryDTO>
            = adminApi.getTop3RentedCarsFromDateToDate(reportRequestDTO)

    override suspend fun getTop3RatingCars(): List<CarSummaryDTO>
            = adminApi.getTop3RatingCars()

    override suspend fun getTop3RatingCarsFromDateToDate(reportRequestDTO: ReportRequestDTO): List<CarSummaryDTO>
            = adminApi.getTop3RatingCarsFromDateToDate(reportRequestDTO)

    override suspend fun getMonthlyTotalRevenue(year: Int): List<MonthlyReportDTO>
            = adminApi.getMonthlyTotalRevenue(year)

    override suspend fun getMonthlyTotalPenalty(year: Int): List<MonthlyReportDTO>
            = adminApi.getMonthlyTotalPenalty(year)

    override suspend fun getTotalRevenueFromDateToDate(reportRequestDTO: ReportRequestDTO): Double
            = adminApi.getTotalRevenueFromDateToDate(reportRequestDTO)

    override suspend fun getTotalPenaltyFromDateToDate(reportRequestDTO: ReportRequestDTO): Double
            = adminApi.getTotalPenaltyFromDateToDate(reportRequestDTO)

    override suspend fun getTotalRevenue(): Double
            = adminApi.getTotalRevenue()

    override suspend fun getTotalPenalty(): Double
            = adminApi.getTotalPenalty()

    override suspend fun getContractSummary(): List<ContractSummaryDTO>
            = adminApi.getContractSummary()

    override suspend fun getContractSummaryFromDateToDate(reportRequestDTO: ReportRequestDTO): List<ContractSummaryDTO>
            = adminApi.getContractSummaryFromDateToDate(reportRequestDTO)

    override suspend fun getReturnStatus(): List<ReturnStatusDTO>
            = adminApi.getReturnStatus()

    override suspend fun getReturnStatusFromDateToDate(reportRequestDTO: ReportRequestDTO): List<ReturnStatusDTO>
            = adminApi.getReturnStatusFromDateToDate(reportRequestDTO)
}