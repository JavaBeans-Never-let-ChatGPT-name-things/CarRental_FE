package com.example.carrental_fe.screen.employee.employeeFullContract

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.EmployeeRepository
import com.example.carrental_fe.model.Contract
import com.example.carrental_fe.model.enums.ReturnCarStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FullContractViewModel (private val employeeRepository: EmployeeRepository): ViewModel() {
    private val _contracts = MutableStateFlow<List<Contract>>(emptyList())
    val contracts : StateFlow<List<Contract>> = _contracts

    private val _filterOptions = MutableStateFlow<List<String>>(emptyList())
    val filterOptions: StateFlow<List<String>> = _filterOptions

    private val _selectedFilter = MutableStateFlow("All User")
    val selectedFilter : StateFlow<String> = _selectedFilter

    private val _selectedSort = MutableStateFlow("StartDate ASC")
    val selectedSort : StateFlow<String> = _selectedSort

    private val _filteredContracts = MutableStateFlow<List<Contract>>(emptyList())
    val filteredContracts : StateFlow<List<Contract>> = _filteredContracts

    init {
        getContracts()
        viewModelScope.launch {
            contracts.collect { updateFilteredContracts() }
        }
    }
    private fun getContracts() {
        viewModelScope.launch {
            try {
                _contracts.value = employeeRepository.getEmployeeContracts()
                _filterOptions.value = listOf("All User") + _contracts.value.map { it.customerName }.distinct()
            }
            catch(e: Exception){
                e.printStackTrace()
            }
        }
    }
    fun confirmPickup(contractId: Long) {
        viewModelScope.launch {
            try {
                employeeRepository.confirmPickup(contractId)
                getContracts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun confirmReturn(contractId: Long, returnCarStatus: ReturnCarStatus) {
        viewModelScope.launch {
            try {
                employeeRepository.confirmReturn(contractId, returnCarStatus)
                getContracts()
            } catch (e: Exception) {
                e.printStackTrace()
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
    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val employeeRepository = (this[APPLICATION_KEY] as CarRentalApplication).container.employeeRepository
                FullContractViewModel(employeeRepository)
            }
        }
    }
}