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
    private val _filterOptions = MutableStateFlow<List<String>>(emptyList())
    val filterOptions: StateFlow<List<String>> = _filterOptions

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter : StateFlow<String> = _selectedFilter

    private val _selectedSort = MutableStateFlow("StartDate ASC")
    val selectedSort : StateFlow<String> = _selectedSort

    private val _filteredContracts = MutableStateFlow<List<Contract>>(emptyList())
    val filteredContracts : StateFlow<List<Contract>> = _filteredContracts

    private val _showRetryDialog = MutableStateFlow(false)
    val showRetryDialog: StateFlow<Boolean> = _showRetryDialog

    private val _selectedContractId = MutableStateFlow<Long?>(null)

    val reviewComment = MutableStateFlow("")
    val reviewStars = MutableStateFlow(0)
    init {
        fetchContracts()
        viewModelScope.launch {
            contracts.collect { updateFilteredContracts() }
        }
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
            _filterOptions.value = listOf("All") + _contracts.value.map { it.contractStatus.name }.distinct()
        }
    }
    fun dismissReviewDialog() {
        _showReviewDialog.value = false
    }
    fun dismissRetryDialog() {
        _showRetryDialog.value = false
    }
    fun retryContract(contractId: Long, carId: String, onCheckoutNav: (String, String, Long?) -> Unit) {
        viewModelScope.launch {
            try {
                val result = accountRepository.retryContract(contractId)
                        if (result.message != "Successfully holded for contract id: $contractId"){
                            _showRetryDialog.value = true
                            return@launch
                        }
                val checkoutUrl = payOsRepository.getCheckoutUrl(ItemRequest(
                    name = "Retry payment for contract with car name: $carId",
                    quantity = 1,
                    price = 2000.0
                )).checkoutUrl
                onCheckoutNav(checkoutUrl, carId, contractId)
            } catch (e: Exception) {
                Log.e("Retry error", "Error: ${e.message}")
            }
        }
    }
    fun reportLostContract(contractId: Long) {
        viewModelScope.launch {
            try {
                val res = accountRepository.reportLost(contractId)
                Log.d("ReportLost", res.message)
                fetchContracts()
            } catch (e: Exception) {
                Log.e("ReportLost error", e.message ?: "Unknown error")
            }
        }
    }

    fun extendContract(contractId: Long, extraDays: Int) {
        viewModelScope.launch {
            try {
                val res = accountRepository.extendContract(contractId, extraDays)
                Log.d("ExtendContract", res.message)
                fetchContracts()
            } catch (e: Exception) {
                Log.e("ExtendContract error", e.message ?: "Unknown error")
            }
        }
    }
    fun setFilter(filter: String) {
        _selectedFilter.value = filter
        updateFilteredContracts()
    }

    fun setSort(sort: String) {
        _selectedSort.value = sort
        updateFilteredContracts()
    }

    private fun updateFilteredContracts() {
        val currentContracts = _contracts.value
        val filtered = if (_selectedFilter.value == "All") {
            currentContracts
        } else {
            currentContracts.filter { it.contractStatus.name == _selectedFilter.value }
        }

        val sorted = when (_selectedSort.value) {
            "StartDate ASC" -> filtered.sortedBy { it.startDate }
            "StartDate DESC" -> filtered.sortedByDescending { it.startDate }
            else -> filtered
        }

        _filteredContracts.value = sorted
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