package com.example.carrental_fe.data

import android.content.Context
import com.example.carrental_fe.network.AccountApi
import com.example.carrental_fe.network.AuthApi
import com.example.carrental_fe.network.CarApi
import com.example.carrental_fe.network.NotificationApi
import com.example.carrental_fe.network.PayOsApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val authenticationRepository: AuthenticationRepository
    val carRepository: CarRepository
    val accountRepository: AccountRepository
    val payOsRepository: PayOsRepository
    val notificationRepository: NotificationRepository
    val adminRepository: AdminRepository
    val employeeRepository: EmployeeRepository
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
    private val accountApi: AccountApi = updatedRetrofit.create(AccountApi::class.java)
    private val payOsApi: PayOsApi = updatedRetrofit.create(PayOsApi::class.java)
    private val notificationApi: NotificationApi = updatedRetrofit.create(NotificationApi::class.java)
    private val adminApi: AdminApi = updatedRetrofit.create(AdminApi::class.java)
    private val employeeApi: EmployeeApi = updatedRetrofit.create(EmployeeApi::class.java)

    override val employeeRepository: EmployeeRepository by lazy {
        EmployeeRepositoryImpl(employeeApi)
    }


    override val authenticationRepository: AuthenticationRepository by lazy {
        AuthenticationRepositoryImpl(authApi)
    }

    override val carRepository: CarRepository by lazy {
        CarRepositoryImpl(carApi)
    }
    override val accountRepository: AccountRepository by lazy {
        AccountRepositoryImpl(accountApi)
    }
    override val payOsRepository: PayOsRepository by lazy {
        PayOsRepositoryImpl(payOsApi)
    }
    override val notificationRepository: NotificationRepository by lazy {
        NotificationRepositoryImpl(notificationApi)
    }
    override val adminRepository: AdminRepository by lazy {
        AdminRepositoryImpl(adminApi)
    }
}