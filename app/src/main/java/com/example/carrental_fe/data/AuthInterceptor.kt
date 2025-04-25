package com.example.carrental_fe.data

import android.util.Log
import com.example.carrental_fe.network.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (originalRequest.url.encodedPath.contains("/api/auth/refresh")) {
            return chain.proceed(originalRequest)
        }
        val requestBuilder = chain.request().newBuilder()
        runBlocking {
            val token = tokenManager.getAccessToken()
            token?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}


class TokenAuthenticator(
    private val tokenManager: TokenManager,
    private val authApi: AuthApi
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val response = chain.proceed(original)
        if (response.code == 401) {
            response.close()
            val newAccessToken = runBlocking {
                val refreshToken = tokenManager.getRefreshToken()
                if (!refreshToken.isNullOrEmpty()) {
                    try {
                        val refreshed = authApi.refresh("Bearer $refreshToken")
                        tokenManager.saveTokens(
                            refreshed.accessToken,
                            refreshed.refreshToken,
                            refreshed.role
                        )
                        refreshed.accessToken
                    } catch (e: Exception) {
                        null
                    }
                } else null
            }

            newAccessToken?.let {
                val newRequest = original.newBuilder()
                    .header("Authorization", "Bearer $it")
                    .build()
                return chain.proceed(newRequest)
            }
        }

        return response
    }
}