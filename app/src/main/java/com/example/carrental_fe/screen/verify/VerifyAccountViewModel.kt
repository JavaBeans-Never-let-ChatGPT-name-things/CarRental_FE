package com.example.carrental_fe.screen.verify

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AuthenticationRepository
import com.example.carrental_fe.data.TokenManager
import com.example.carrental_fe.dto.request.EmailRequest
import com.example.carrental_fe.dto.request.VerifyUserRequest
import com.example.carrental_fe.dto.response.TokenResponse
import com.example.carrental_fe.nav.VerifyAccount
import com.example.carrental_fe.screen.signup.SignUpViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class VerifyAccountState {
    object Idle : VerifyAccountState()
    object Loading : VerifyAccountState()
    data class Success(val tokenResponse: TokenResponse) : VerifyAccountState()
    data class Error(val message: String) : VerifyAccountState()
}
class VerifyAccountViewModel (private val authenticationRepository: AuthenticationRepository,
    savedStateHandle: SavedStateHandle, private val tokenManager: TokenManager): ViewModel() {
    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode

    private val _verifyAccountState = MutableStateFlow<com.example.carrental_fe.screen.verify.VerifyAccountState>(
        com.example.carrental_fe.screen.verify.VerifyAccountState.Idle)
    val verifyAccountState: StateFlow<com.example.carrental_fe.screen.verify.VerifyAccountState> = _verifyAccountState

    private val email = savedStateHandle.toRoute<VerifyAccount>()

    fun onVerificationCodeChange(newVerificationCode: String) {
        _verificationCode.value = newVerificationCode
    }

    fun resendVerificationCode()
    {
        viewModelScope.launch {
            _verifyAccountState.value = com.example.carrental_fe.screen.verify.VerifyAccountState.Loading
            try {
                val response = authenticationRepository.resend(EmailRequest(email.email?:""))
                Log.d("VerifyAccountViewModel", "resendVerificationCode: $response")
            }
            catch (e: Exception) {
                _verifyAccountState.value = com.example.carrental_fe.screen.verify.VerifyAccountState.Error(e.message?: "An error occurred")
            }
        }
    }
    fun verifyAccount()
    {
        viewModelScope.launch {
            _verifyAccountState.value = com.example.carrental_fe.screen.verify.VerifyAccountState.Loading
            try {
                val response = authenticationRepository.verify(VerifyUserRequest(
                    email.email?:"",
                    _verificationCode.value
                ))
                _verifyAccountState.value = com.example.carrental_fe.screen.verify.VerifyAccountState.Success(response)
                tokenManager.saveTokens(
                    response.accessToken,
                    response.refreshToken,
                    response.role
                )
            }
            catch (e: Exception) {
                _verifyAccountState.value = com.example.carrental_fe.screen.verify.VerifyAccountState.Error(e.message?: "An error occurred")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val authenticationRepository = application.container.authenticationRepository
                val savedStateHandle = createSavedStateHandle()
                val tokenManager = TokenManager(application)
                VerifyAccountViewModel(authenticationRepository = authenticationRepository, savedStateHandle = savedStateHandle,
                    tokenManager = tokenManager)
            }
        }
    }
}