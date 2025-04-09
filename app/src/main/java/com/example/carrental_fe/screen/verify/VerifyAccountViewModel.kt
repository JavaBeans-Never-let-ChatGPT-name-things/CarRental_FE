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
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AuthenticationRepository
import com.example.carrental_fe.dto.request.EmailRequest
import com.example.carrental_fe.dto.request.VerifyUserRequest
import com.example.carrental_fe.dto.response.TokenResponse
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
class VerifyAccountViewModel (private val authenticationRepository: AuthenticationRepository): ViewModel() {
    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode

    private val _verifyAccountState = MutableStateFlow<VerifyAccountState>(VerifyAccountState.Idle)
    val verifyAccountState: StateFlow<VerifyAccountState> = _verifyAccountState
    lateinit var email: String

    fun onVerificationCodeChange(newVerificationCode: String) {
        _verificationCode.value = newVerificationCode
    }

    fun resendVerificationCode() {
        viewModelScope.launch {
            try {
                val response = authenticationRepository.resend(EmailRequest(email))
                Log.d("VerifyAccountViewModel", "resendVerificationCode: $response")
            } catch (e: Exception) {
                Log.d("VerifyAccountViewModel", "resendVerificationCode: $e")
            }
        }
    }

    fun verifyAccount() {
        viewModelScope.launch {
            try {
                val response = authenticationRepository.verify(
                    VerifyUserRequest(
                        email,
                        _verificationCode.value
                    )
                )
                _verifyAccountState.value = VerifyAccountState.Success(response)
            } catch (e: Exception) {
                Log.d("VerifyAccountViewModel", "verifyAccount: $e")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val authenticationRepository = application.container.authenticationRepository
                VerifyAccountViewModel(authenticationRepository = authenticationRepository)
            }
        }
    }
}