package com.example.carrental_fe.data

import com.example.carrental_fe.dto.request.CarPageRequestDTO
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Car
import com.example.carrental_fe.model.CarBrand
import com.example.carrental_fe.model.Review
import com.example.carrental_fe.network.CarApi
import retrofit2.Response

class CarRepositoryImpl (private val carApi: CarApi): CarRepository {
    override suspend fun getCarBrands(): Response<List<CarBrand>>
            = carApi.getCarBrands()

    override suspend fun getCarPage(carPageRequestDTO: CarPageRequestDTO): Response<List<Car>>
            = carApi.getCarPage(carPageRequestDTO)

    override suspend fun getCarPageByBrand(
        carPageRequestDTO: CarPageRequestDTO,
        brandId: Long
    ): Response<List<Car>>
            = carApi.getCarPageByBrand(carPageRequestDTO, brandId)

    override suspend fun getCount(): Response<Long>
            = carApi.getCarCount()

    override suspend fun getCountByBrand(brandId: Long): Response<Long>
            = carApi.getCarCountByBrand(brandId)

    override suspend fun updateFavourite(carId: String): MessageResponse
            = carApi.addToFavourites(carId)

    override suspend fun getFavourites(): Response<List<Car>>
            = carApi.getFavourites()

    override suspend fun countFilter(carId: String): Response<Long>
            = carApi.getFilterCount(carId)

    override suspend fun getFilterCarPageByBrand(
        carPageRequestDTO: CarPageRequestDTO,
        carId: String
    ): Response<List<Car>>
            = carApi.getFilterCarPageByBrand(carPageRequestDTO, carId)

    override suspend fun getCarReviews(carId: String): Response<List<Review>>
            = carApi.getCarReviews(carId)

    override suspend fun getCarDetail(carId: String): Response<Car>
            = carApi.getCarDetail(carId)

    override suspend fun getCarBrandByName(): List<String>
            = carApi.getCarBrandByName()

    override suspend fun getAllCarsName(): List<String>
            = carApi.getAllCarsName()

}