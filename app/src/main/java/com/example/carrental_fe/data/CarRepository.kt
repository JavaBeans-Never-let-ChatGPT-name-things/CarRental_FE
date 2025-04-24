package com.example.carrental_fe.data

import com.example.carrental_fe.dto.request.CarPageRequestDTO
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Car
import com.example.carrental_fe.model.CarBrand
import com.example.carrental_fe.model.Review
import retrofit2.Response

interface CarRepository {
    suspend fun getCarBrands(): Response<List<CarBrand>>
    suspend fun getCarPage(carPageRequestDTO: CarPageRequestDTO): Response<List<Car>>
    suspend fun getCarPageByBrand(carPageRequestDTO: CarPageRequestDTO, brandId: Long): Response<List<Car>>
    suspend fun getCount(): Response<Long>
    suspend fun getCountByBrand(brandId: Long): Response<Long>
    suspend fun updateFavourite(carId: String) : MessageResponse
    suspend fun getFavourites(): Response<List<Car>>
    suspend fun countFilter(carId: String): Response<Long>
    suspend fun getFilterCarPageByBrand(carPageRequestDTO: CarPageRequestDTO, carId: String): Response<List<Car>>
    suspend fun getCarReviews(carId: String): Response<List<Review>>
    suspend fun getCarDetail(carId: String): Response<Car>
}