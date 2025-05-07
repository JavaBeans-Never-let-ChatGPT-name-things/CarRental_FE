package com.example.carrental_fe.screen.user.userContractDetail

import android.net.Uri
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
import com.example.carrental_fe.data.AccountRepository
import com.example.carrental_fe.data.PayOsRepository
import com.example.carrental_fe.dto.request.ContractRequestDTO
import com.example.carrental_fe.dto.request.ItemRequest
import com.example.carrental_fe.model.Account
import com.example.carrental_fe.model.enums.PaymentStatus
import com.example.carrental_fe.nav.ContractDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ContractDetailViewModel(private val accountRepository: AccountRepository,
                              private val payOsRepository: PayOsRepository,
                              savedStateHandle: SavedStateHandle) : ViewModel() {
    private val route = savedStateHandle.toRoute<ContractDetail>()
    val pricePreday = route.carPrice
    val carId = route.carId
    private val _contractId = MutableStateFlow<Long>(0)
    private val _checkoutUrl = MutableStateFlow<String>("")
    private val _account = MutableStateFlow<Account?>(null)
    val account = _account.asStateFlow()

    private val _startDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<LocalDate>(LocalDate.now().plusDays(1))
    val endDate = _endDate.asStateFlow()

    private val _totalPrice = MutableStateFlow<Float>(0f)
    val totalPrice = _totalPrice.asStateFlow()

    private val _dateDiff = MutableStateFlow<Long>(0)
    val dateDiff = _dateDiff.asStateFlow()

    private fun observeDateChanges() {
        viewModelScope.launch {
            combine(_startDate, _endDate) { start, end ->
                val days = ChronoUnit.DAYS.between(start, end).coerceAtLeast(0)
                val total = days * pricePreday!!
                Pair(days, total)
            }.collect { (days, total) ->
                _dateDiff.value = days
                _totalPrice.value = total
            }
        }
    }
    fun setStartDate(date: LocalDate) {
        _startDate.value = date
    }
    fun setEndDate(date: LocalDate) {
        _endDate.value = date
    }
    init {
        setInitialData()
        observeDateChanges()
    }

    fun setInitialData() {
        viewModelScope.launch {
            val accountResponse = accountRepository.getAccount()
            if (accountResponse.isSuccessful) {
                _account.value = accountResponse.body()
            }
        }
    }
    fun createContract(onNav: (String, String, Long) -> Unit){
        viewModelScope.launch{
            _checkoutUrl.value = payOsRepository.getCheckoutUrl(ItemRequest(
                name = "Car Rental Deposit for $carId",
                quantity = 1,
                price = 2000.0
            )).checkoutUrl
            val startDate = _startDate.value
            val endDate = _endDate.value
            val deposit = _totalPrice.value
            val paymentMethod = "PayOS"
            val paymentStatus = PaymentStatus.FAILED
            val totalPrice = _totalPrice.value
            val contractRequestDTO = ContractRequestDTO(
                startDate,
                endDate,
                deposit,
                paymentMethod,
                paymentStatus,
                totalPrice
            )
            val result = accountRepository.rentCar(carId?:"", contractRequestDTO).body()
            _contractId.value = result ?: 0
            onNav(Uri.encode(_checkoutUrl.value), carId?:"", _contractId.value)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val accountRepository =
                    (this[APPLICATION_KEY] as CarRentalApplication).container.accountRepository
                val payOsRepository = (this[APPLICATION_KEY] as CarRentalApplication).container.payOsRepository
                val savedStateHandle = createSavedStateHandle()
                ContractDetailViewModel(accountRepository, payOsRepository, savedStateHandle)
            }
        }
    }
}