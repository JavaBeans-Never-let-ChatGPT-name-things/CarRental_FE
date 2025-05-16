package com.example.carrental_fe.screen.admin.adminUserDetail

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
import com.example.carrental_fe.data.AdminRepository
import com.example.carrental_fe.dto.response.UserDetailDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserDetailViewModel(private val adminRepository: AdminRepository,
                          savedStateHandle: SavedStateHandle): ViewModel() {
    private val _user = MutableStateFlow<UserDetailDTO?>(null)
    val user: StateFlow<UserDetailDTO?> = _user
    init
    {
        fetchUser()
    }
    fun fetchUser()
    {
        viewModelScope.launch {
            try{
                Log.d("UserDetailViewModel", "User fetched: ${_user.value?.rentalContracts?.size?:0} ${_user.value?.gender}")
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as CarRentalApplication
                val savedStateHandle = createSavedStateHandle()
                UserDetailViewModel(app.container.adminRepository, savedStateHandle)
            }
        }
    }
}