package com.example.carrental_fe.data

import com.example.carrental_fe.network.AuthApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val authenticationRepository: AuthenticationRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl =
        "https://spaniel-genuine-muskrat.ngrok-free.app"

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log toàn bộ request và response body
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Thêm LoggingInterceptor vào OkHttpClient
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }
    override val authenticationRepository: AuthenticationRepository by lazy{
        AuthenticationRepositoryImpl(retrofitService)
    }
}