package com.example.carrental_fe.data

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
}