package com.example.carrental_fe.screen.employee.emplooyeeFullContract

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

    init {
        getContracts()
    }
    private fun getContracts() {
        viewModelScope.launch {
            try {
                _contracts.value = employeeRepository.getEmployeeContracts()
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
    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val employeeRepository = (this[APPLICATION_KEY] as CarRentalApplication).container.employeeRepository
                FullContractViewModel(employeeRepository)
            }
        }
    }
}