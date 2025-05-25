package com.example.carrental_fe.data

import com.example.carrental_fe.dto.request.ReportRequestDTO
import com.example.carrental_fe.dto.response.CarSummaryDTO
import com.example.carrental_fe.dto.response.ContractSummaryDTO
import com.example.carrental_fe.dto.response.MonthlyReportDTO
import com.example.carrental_fe.dto.response.ReturnStatusDTO
import com.example.carrental_fe.dto.response.UserSummaryDTO
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

    suspend fun getTop3BestUsers(): List<UserSummaryDTO>
    suspend fun getTop3WorstUsers(): List<UserSummaryDTO>
    suspend fun getTop3BestUsersFromDateToDate(reportRequestDTO: ReportRequestDTO) : List<UserSummaryDTO>
    suspend fun getTop3WorstUsersFromDateToDate(reportRequestDTO: ReportRequestDTO) : List<UserSummaryDTO>
    suspend fun getTop3RentedCars(): List<CarSummaryDTO>
    suspend fun getTop3RentedCarsFromDateToDate(reportRequestDTO: ReportRequestDTO) : List<CarSummaryDTO>
    suspend fun getTop3RatingCars(): List<CarSummaryDTO>
    suspend fun getTop3RatingCarsFromDateToDate(reportRequestDTO: ReportRequestDTO) : List<CarSummaryDTO>

    suspend fun getMonthlyTotalRevenue(year: Int): List<MonthlyReportDTO>
    suspend fun getMonthlyTotalPenalty(year: Int): List<MonthlyReportDTO>

    suspend fun getTotalRevenueFromDateToDate(reportRequestDTO: ReportRequestDTO): Double
    suspend fun getTotalPenaltyFromDateToDate(reportRequestDTO: ReportRequestDTO): Double

    suspend fun getTotalRevenue(): Double
    suspend fun getTotalPenalty(): Double

    suspend fun getContractSummary(): List<ContractSummaryDTO>
    suspend fun getContractSummaryFromDateToDate(reportRequestDTO: ReportRequestDTO): List<ContractSummaryDTO>

    suspend fun getReturnStatus(): List<ReturnStatusDTO>
    suspend fun getReturnStatusFromDateToDate(reportRequestDTO: ReportRequestDTO): List<ReturnStatusDTO>
}