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
    val carRepository: CarRepository
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val baseUrl = "https://spaniel-genuine-muskrat.ngrok-free.app"
    private val tokenManager = TokenManager(context)

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val authApi: AuthApi = retrofit.create(AuthApi::class.java)

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenManager))
        .addInterceptor(TokenAuthenticator(tokenManager, authApi))
        .build()

    private val updatedRetrofit: Retrofit = retrofit.newBuilder()
        .client(okHttpClient)
        .build()

    private val carApi: CarApi = updatedRetrofit.create(CarApi::class.java)

    override val authenticationRepository: AuthenticationRepository by lazy {
        AuthenticationRepositoryImpl(authApi)
    }

    override val carRepository: CarRepository by lazy {
        CarRepositoryImpl(carApi)
    }
}