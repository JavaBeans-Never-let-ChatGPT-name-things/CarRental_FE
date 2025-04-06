package com.example.carrental_fe.screen.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AuthenticationRepository
import com.example.carrental_fe.dto.request.EmailRequest
import com.example.carrental_fe.dto.response.MessageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ForgotPasswordState {
    object Idle : ForgotPasswordState()
    object Loading : ForgotPasswordState()
    data class Success(val messageResponse: MessageResponse) : ForgotPasswordState()
    data class Error(val message: String) : ForgotPasswordState()
}
class ForgotPasswordViewModel (private val authenticationRepository: AuthenticationRepository): ViewModel(){
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _forgotPasswordState = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Idle)
    val forgotPasswordState: StateFlow<ForgotPasswordState> = _forgotPasswordState

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }
    fun sendEmail()
    {
        viewModelScope.launch {
            _forgotPasswordState.value = ForgotPasswordState.Loading
            try {
                val response = authenticationRepository.forgot(
                    EmailRequest(_email.value)
                )
                if(response.message == "Forgot password email sent")
                {
                    _forgotPasswordState.value = ForgotPasswordState.Success(response)
                }
            }
            catch (e: Exception)
            {
                _forgotPasswordState.value = ForgotPasswordState.Error(e.message?: "An error occurred")
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val authenticationRepository = application.container.authenticationRepository
                ForgotPasswordViewModel(authenticationRepository = authenticationRepository)
            }
        }
    }
}