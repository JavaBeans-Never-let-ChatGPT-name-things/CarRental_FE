package com.example.carrental_fe.screen.employee.employeeContractList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.EmployeeRepository
import com.example.carrental_fe.model.Contract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConfirmContractViewModel(private val employeeRepository: EmployeeRepository): ViewModel() {
    private val _contracts = MutableStateFlow<List<Contract>>(emptyList())
    val contracts : StateFlow<List<Contract>> = _contracts

    init {
        fetchContracts()
    }
    fun fetchContracts() {
        viewModelScope.launch {
            try {
                _contracts.value = employeeRepository.getPendingContracts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun confirmContract(contractId: Long) {
        viewModelScope.launch {
            try {
                employeeRepository.confirmAssignment(contractId)
                fetchContracts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun rejectContract(contractId: Long) {
        viewModelScope.launch {
            try {
                employeeRepository.rejectAssignment(contractId)
                fetchContracts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val employeeRepository = (this[APPLICATION_KEY] as CarRentalApplication).container.employeeRepository
                ConfirmContractViewModel(employeeRepository)
            }
        }
    }
}