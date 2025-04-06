package com.example.carrental_fe.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AuthenticationRepository
import com.example.carrental_fe.dto.request.RegisterRequest
import com.example.carrental_fe.dto.response.MessageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    data class Success(val messageResponse: MessageResponse) : SignUpState()
    data class Error(val message: String) : SignUpState()
}
class SignUpViewModel (private val authenticationRepository: AuthenticationRepository) : ViewModel(){
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _displayName = MutableStateFlow("")
    val displayName: StateFlow<String> = _displayName

    private val _email = MutableStateFlow("")
    val email : StateFlow<String> = _email

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onDisplayNameChange(newDisplayName: String) {
        _displayName.value = newDisplayName
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun resetFields() {
        _username.value = ""
        _password.value = ""
        _displayName.value = ""
        _email.value = ""
    }

    fun register()
    {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {
                val response = authenticationRepository.register(
                    RegisterRequest(
                        _username.value,
                        _password.value,
                        _email.value,
                        _displayName.value
                    )
                )
                _signUpState.value = SignUpState.Success(response)
            } catch (e: Exception) {
                _signUpState.value = SignUpState.Error(e.message ?: "Registration failed")
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val authenticationRepository = application.container.authenticationRepository
                SignUpViewModel(authenticationRepository = authenticationRepository)
            }
        }
    }
}