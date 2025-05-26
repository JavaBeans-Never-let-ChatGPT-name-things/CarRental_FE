package com.example.carrental_fe.screen.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AuthenticationRepository
import com.example.carrental_fe.data.TokenManager
import com.example.carrental_fe.dto.response.TokenResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoggedInState {
    object Idle : LoggedInState()
    data class Success(val tokenResponse: TokenResponse) : LoggedInState()
    data class Error(val message: String) : LoggedInState()
}
class SplashScreenViewModel(private val authenticationRepository: AuthenticationRepository,
                            private val tokenManager: TokenManager): ViewModel() {
    private val _loggedInState = MutableStateFlow<LoggedInState> (LoggedInState.Idle)
    val loggedInState : StateFlow<LoggedInState> = _loggedInState
    init {
        checkLoginStatus()
    }
    fun checkLoginStatus(){
        viewModelScope.launch {
            val refreshToken = tokenManager.getRefreshToken()
            if (!refreshToken.isNullOrBlank()) {
                try {
                    val response = authenticationRepository.refresh("Bearer $refreshToken")
                    tokenManager.saveTokens(
                        response.accessToken,
                        response.refreshToken,
                        response.role
                    )
                    _loggedInState.value = LoggedInState.Success(response)
                } catch (e: Exception) {
                    _loggedInState.value = LoggedInState.Error(e.message ?: "An error occurred")
                }
            }
            else
            {
                _loggedInState.value = LoggedInState.Error("No refresh token found")
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val authenticationRepository = application.container.authenticationRepository
                val tokenManager = TokenManager(application)
                SplashScreenViewModel(authenticationRepository, tokenManager)
            }
        }
    }
}