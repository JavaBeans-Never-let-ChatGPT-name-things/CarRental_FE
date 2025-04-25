package com.example.carrental_fe.screen.userCheckout

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
import com.example.carrental_fe.data.PayOsRepository
import com.example.carrental_fe.nav.PaymentWebView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(private val payOsRepository: PayOsRepository,
                        savedStateHandle: SavedStateHandle): ViewModel() {
    private val route = savedStateHandle.toRoute<PaymentWebView>()
    private val _url = MutableStateFlow(Uri.decode(route.url))
    val checkoutUrl = _url.asStateFlow()
    val contractId = route.contractId
    val carId = route.carId

    fun paymentSuccess(){
        viewModelScope.launch {
            payOsRepository.paymentSuccess(contractId?:0)
        }
    }
    fun paymentFailed(){
        viewModelScope.launch {
            payOsRepository.paymentFailed(carId?:"")
        }
    }
    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory{
            initializer {
                val payOsRepository = (this[APPLICATION_KEY] as CarRentalApplication).container.payOsRepository
                val savedStateHandle = createSavedStateHandle()
                CheckoutViewModel(payOsRepository, savedStateHandle)
            }
        }
    }
}