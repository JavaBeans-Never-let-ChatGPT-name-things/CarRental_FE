package com.example.carrental_fe.screen.user.userCheckout

import android.net.Uri
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
import com.example.carrental_fe.data.AccountRepository
import com.example.carrental_fe.data.PayOsRepository
import com.example.carrental_fe.nav.PaymentWebView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(private val payOsRepository: PayOsRepository,
                        private val accountRepository: AccountRepository,
                        savedStateHandle: SavedStateHandle): ViewModel() {
    private val route = savedStateHandle.toRoute<PaymentWebView>()
    private val _url = MutableStateFlow(Uri.decode(route.url))
    val checkoutUrl = _url.asStateFlow()
    val contractId = route.contractId
    val carId = route.carId
    val isRetry = route.isRetry

    fun paymentSuccess(){
        viewModelScope.launch {
            if (!isRetry!!){
                payOsRepository.paymentSuccess(contractId?:0)
            }
            else
            {
                accountRepository.retrySuccess(contractId?:0)
            }
        }
    }
    fun paymentFailed(){
        viewModelScope.launch {
            if (!isRetry!!){
                payOsRepository.paymentFailed(carId?:"")
            }
        }
    }
    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory{
            initializer {
                val payOsRepository = (this[APPLICATION_KEY] as CarRentalApplication).container.payOsRepository
                val accountRepository = (this[APPLICATION_KEY] as CarRentalApplication).container.accountRepository
                val savedStateHandle = createSavedStateHandle()
                CheckoutViewModel(payOsRepository,accountRepository, savedStateHandle)
            }
        }
    }
}