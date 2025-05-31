package com.example.carrental_fe.screen.admin.adminContractList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AdminRepository
import com.example.carrental_fe.model.Contract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PendingContractViewModel(private val adminRepository: AdminRepository): ViewModel() {
    private val _pendingContracts = MutableStateFlow<List<Contract>>(emptyList())
    val pendingContracts: StateFlow<List<Contract>> = _pendingContracts

    private val _employees = MutableStateFlow<List<String>>(emptyList())
    val employees: StateFlow<List<String>> = _employees

    private val _filterOptions = MutableStateFlow<List<String>>(emptyList())
    val filterOptions: StateFlow<List<String>> = _filterOptions

    private val _selectedFilter = MutableStateFlow("All User")
    val selectedFilter : StateFlow<String> = _selectedFilter

    private val _selectedSort = MutableStateFlow("StartDate ASC")
    val selectedSort : StateFlow<String> = _selectedSort

    private val _filteredContracts = MutableStateFlow<List<Contract>>(emptyList())
    val filteredContracts : StateFlow<List<Contract>> = _filteredContracts
    init {
        fetchContracts()
        viewModelScope.launch { updateFilteredContracts() }
    }
    fun fetchContracts()
    {
        viewModelScope.launch {
            try{
                _pendingContracts.value = adminRepository.getContracts()
                _filterOptions.value = listOf("All User") + _pendingContracts.value.map { it.customerName }.distinct()
            }
            catch(e: Exception)
            {
                Log.d("Pending Contracts", e.message.toString())
            }
        }
    }
    fun fetchAvailableEmployees(contractId: Long)
    {
        viewModelScope.launch {
            try{
                _employees.value = adminRepository.getAvailableEmployees(contractId)
            }
            catch(e: Exception)
            {
                Log.d("Available Employees", e.message.toString())
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
        val currentContracts = _pendingContracts.value
        val filtered = if (_selectedFilter.value == "All User") {
            currentContracts
        } else {
            currentContracts.filter { it.customerName == _selectedFilter.value }
        }

        val sorted = when (_selectedSort.value) {
            "StartDate ASC" -> filtered.sortedBy { it.startDate }
            "StartDate DESC" -> filtered.sortedByDescending { it.startDate }
            else -> filtered
        }

        _filteredContracts.value = sorted
    }
    fun assignContract(contractId: Long, employeeName: String)
    {
        viewModelScope.launch {
            try{
                adminRepository.assignContract(contractId, employeeName)
            }
            catch(e: Exception)
            {
                Log.d("Assign Contract", e.message.toString())
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory{
            initializer {
                val adminRepository: AdminRepository = (this[APPLICATION_KEY] as CarRentalApplication).container.adminRepository
                PendingContractViewModel(adminRepository = adminRepository)
            }
        }
    }
}