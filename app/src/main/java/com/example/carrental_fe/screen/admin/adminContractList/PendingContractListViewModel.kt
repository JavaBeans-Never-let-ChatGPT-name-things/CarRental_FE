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

    init {
        fetchContracts()
    }
    fun fetchContracts()
    {
        viewModelScope.launch {
            try{
                _pendingContracts.value = adminRepository.getContracts()
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