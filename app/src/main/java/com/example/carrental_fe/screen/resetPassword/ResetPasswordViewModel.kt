package com.example.carrental_fe.screen.resetPassword

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
import com.example.carrental_fe.dto.request.ResetPasswordRequest
import com.example.carrental_fe.dto.response.MessageResponse
import com.example.carrental_fe.nav.ResetPassword
import com.example.carrental_fe.screen.verify.VerifyAccountViewModel
import com.example.carrental_fe.utils.isValidPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ResetPasswordState {
    object Idle : ResetPasswordState()
    object Loading : ResetPasswordState()
    data class Success(val messageResponse: MessageResponse) : ResetPasswordState()
    data class Error(val message: String) : ResetPasswordState()
}
class ResetPasswordViewModel(private val authenticationRepository: AuthenticationRepository,
                             savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val email = savedStateHandle.toRoute<ResetPassword>()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible

    private val _resetPasswordState = MutableStateFlow<ResetPasswordState>(ResetPasswordState.Idle)
    val resetPasswordState: StateFlow<ResetPasswordState> = _resetPasswordState

    private val _errorMessage = MutableStateFlow<String?>("")
    val errorMessage: StateFlow<String?> = _errorMessage

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onVerificationCodeChange(newVerificationCode: String) {
        _verificationCode.value = newVerificationCode
    }
    fun resetPassword() {
        viewModelScope.launch {
            try {
                if (!_password.value.isValidPassword()){
                    _errorMessage.value = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
                    return@launch
                }
                _resetPasswordState.value = ResetPasswordState.Loading
                val response = authenticationRepository.reset(ResetPasswordRequest(
                    email = email.email?:"",
                    verificationCode = _verificationCode.value,
                    newPassword = _password.value
                ))
                _resetPasswordState.value = ResetPasswordState.Success(response)
            } catch (e: Exception) {
                _resetPasswordState.value = ResetPasswordState.Error(e.message ?: "An error occurred")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val authenticationRepository = application.container.authenticationRepository
                val savedStateHandle = createSavedStateHandle()
                ResetPasswordViewModel(authenticationRepository = authenticationRepository, savedStateHandle = savedStateHandle)
            }
        }
    }
}