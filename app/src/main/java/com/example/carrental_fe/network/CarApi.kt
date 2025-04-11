package com.example.carrental_fe.network

import com.example.carrental_fe.dto.request.CarPageRequestDTO
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.model.Car
import com.example.carrental_fe.model.CarBrand
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CarApi {
    @GET("/api/cars/brands")
    suspend fun getCarBrands(): Response<List<CarBrand>>

    @POST("api/cars/pagination")
    suspend fun getCarPage(@Body carPageRequestDTO: CarPageRequestDTO): Response<List<Car>>

    @POST("api/cars/pagination/filter/brand/{brandId}")
    suspend fun getCarPageByBrand(
        @Body carPageRequestDTO: CarPageRequestDTO,
        @Path("brandId") brandId: Long
    ): Response<List<Car>>

    @GET("/api/cars/count")
    suspend fun getCarCount(): Response<Long>

    @GET("/api/cars/count/brand/{brandId}")
    suspend fun getCarCountByBrand(@Path("brandId") brandId: Long): Response<Long>

    @PUT("/api/accounts/favourite/{carId}")
    suspend fun addToFavourites(@Path("carId") carId: String): MessageResponse

    @GET("/api/accounts/favourite/")
    suspend fun getFavourites(): Response<List<Car>>

    @GET("/api/cars/count/filter/{carId}")
    suspend fun getFilterCount(
        @Path("carId") carId: String
    ): Response<Long>

    @POST("/api/cars/pagination/filter/{carId}")
    suspend fun getFilterCarPageByBrand(
        @Body carPageRequestDTO: CarPageRequestDTO,
        @Path("carId") carId: String
    ): Response<List<Car>>
}