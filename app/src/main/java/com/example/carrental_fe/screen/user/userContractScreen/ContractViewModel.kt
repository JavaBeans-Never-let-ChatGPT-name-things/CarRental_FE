package com.example.carrental_fe.screen.user.userContractScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AccountRepository
import com.example.carrental_fe.data.PayOsRepository
import com.example.carrental_fe.dto.request.ItemRequest
import com.example.carrental_fe.dto.request.ReviewRequestDTO
import com.example.carrental_fe.model.Contract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContractViewModel (private val accountRepository: AccountRepository,
                         private val payOsRepository: PayOsRepository): ViewModel() {
    private val _contracts = MutableStateFlow<List<Contract>>(emptyList())
    val contracts: StateFlow<List<Contract>> = _contracts
    private val _showReviewDialog = MutableStateFlow(false)
    val showReviewDialog: StateFlow<Boolean> = _showReviewDialog

    private val _selectedContractId = MutableStateFlow<Long?>(null)

    val reviewComment = MutableStateFlow("")
    val reviewStars = MutableStateFlow(0)
    init {
        fetchContracts()
    }

    fun onReviewClick(contractId: Long) {
        _selectedContractId.value = contractId
        _showReviewDialog.value = true
    }

    fun onReviewSubmit() {
        val contractId = _selectedContractId.value ?: return
        viewModelScope.launch {
            Log.d("Review detail", "stars: ${reviewStars.value}, comment: ${reviewComment.value}")
            val review = ReviewRequestDTO(
                starsNum = reviewStars.value,
                comment = reviewComment.value
            )
            try {
                accountRepository.reviewContract(contractId, review)
            }
            catch (e: Exception) {
                Log.e("Review error", "Error: ${e.message}")
            }
            fetchContracts()
            _showReviewDialog.value = false
            reviewStars.value = 0
            reviewComment.value = ""
        }
    }

    fun fetchContracts() {
        viewModelScope.launch {
            _contracts.value = accountRepository.getContracts()
        }
    }
    fun dismissReviewDialog() {
        _showReviewDialog.value = false
    }
    fun retryContract(contractId: Long, carId: String, onCheckoutNav: (String, String, Long?) -> Unit) {
        viewModelScope.launch {
            try {
                val checkoutUrl = payOsRepository.getCheckoutUrl(ItemRequest(
                    name = "Retry payment for contract with car name: $carId",
                    quantity = 1,
                    price = 2000.0
                )).checkoutUrl
                accountRepository.retryContract(contractId)
                onCheckoutNav(checkoutUrl, carId, contractId)
            } catch (e: Exception) {
                Log.e("Retry error", "Error: ${e.message}")
            }
        }
    }
    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val accountRepository = (this[APPLICATION_KEY] as CarRentalApplication).container.accountRepository
                val payOsRepository = (this[APPLICATION_KEY] as CarRentalApplication).container.payOsRepository
                ContractViewModel(accountRepository, payOsRepository)
            }
        }
    }
}