package com.example.carrental_fe.screen.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AccountRepository
import com.example.carrental_fe.model.Account
import com.example.carrental_fe.screen.userSearchScreen.SearchScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileScreenViewModel(private val accountRepository: AccountRepository): ViewModel() {
    private val _accountInfo = MutableStateFlow<Account?>(null)
    val accountInfo: StateFlow<Account?> = _accountInfo
    init {
        getAccountInfo()
    }
    fun getAccountInfo() {
        viewModelScope.launch {
            val response = accountRepository.getAccount()
            if (response.isSuccessful)
            {
                _accountInfo.value = response.body()
            }
        }

    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val accountRepository = application.container.accountRepository
                ProfileScreenViewModel(accountRepository = accountRepository)
            }
        }
    }
}