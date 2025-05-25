package com.example.carrental_fe.screen.admin.adminAnalytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AdminRepository
import com.example.carrental_fe.dto.request.ReportRequestDTO
import com.example.carrental_fe.dto.response.CarSummaryDTO
import com.example.carrental_fe.dto.response.ContractSummaryDTO
import com.example.carrental_fe.dto.response.MonthlyReportDTO
import com.example.carrental_fe.dto.response.ReturnStatusDTO
import com.example.carrental_fe.dto.response.UserSummaryDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class AnalyticsViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _selectedYear = MutableStateFlow(2025)
    val selectedYear = _selectedYear.asStateFlow()

    private val _startDate = MutableStateFlow<LocalDate?>(null)
    val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<LocalDate?>(null)
    val endDate = _endDate.asStateFlow()

    private val _monthlyRevenue = MutableStateFlow<List<MonthlyReportDTO>> (emptyList())
    val monthlyRevenue = _monthlyRevenue.asStateFlow()

    private val _monthlyPenalty =MutableStateFlow<List<MonthlyReportDTO>> (emptyList())
    val monthlyPenalty = _monthlyPenalty.asStateFlow()

    private val _totalRevenue = MutableStateFlow(0.0)
    val totalRevenue = _totalRevenue.asStateFlow()

    private val _totalPenalty = MutableStateFlow(0.0)
    val totalPenalty = _totalPenalty.asStateFlow()

    private val _contractSummary = MutableStateFlow<List<ContractSummaryDTO>>(emptyList())
    val contractSummary = _contractSummary.asStateFlow()

    private val _returnStatus = MutableStateFlow<List<ReturnStatusDTO>>(emptyList())
    val returnStatus = _returnStatus.asStateFlow()

    private val _carFilterOption = MutableStateFlow("Rental Count")
    val carFilterOption = _carFilterOption.asStateFlow()

    fun updateCarFilter(option: String) {
        _carFilterOption.value = option
        loadTop3Cars()
    }

    private val _top3Cars = MutableStateFlow<List<CarSummaryDTO>>(emptyList())
    val top3Cars = _top3Cars.asStateFlow()

    private val _top3BestUsers = MutableStateFlow<List<UserSummaryDTO>>(emptyList())
    val top3BestUsers = _top3BestUsers.asStateFlow()

    private val _top3WorstUsers = MutableStateFlow<List<UserSummaryDTO>>(emptyList())
    val top3WorstUsers = _top3WorstUsers.asStateFlow()

    init {
        loadAllData()
    }

    fun updateYear(year: Int) {
        _selectedYear.value = year
        loadMonthlyData()
    }

    fun updateDateRange(start: LocalDate?, end: LocalDate?) {
        _startDate.value = start
        _endDate.value = end
        loadAllData()
    }

    fun resetDateRange() {
        _startDate.value = null
        _endDate.value = null
        loadAllData()
    }

    private fun loadAllData() {
        loadMonthlyData()
        loadTotalRevenuePenalty()
        loadContractSummary()
        loadReturnStatus()
        loadTop3Cars()
        loadTop3Users()
    }

    private fun loadMonthlyData() {
        viewModelScope.launch {
            val year = _selectedYear.value
            val revenue = adminRepository.getMonthlyTotalRevenue(year)
            _monthlyRevenue.value = revenue

            val penalty = adminRepository.getMonthlyTotalPenalty(year)
            _monthlyPenalty.value = penalty
        }
    }

    private fun loadTotalRevenuePenalty() {
        viewModelScope.launch {
            if (_startDate.value != null && _endDate.value != null) {
                val request = ReportRequestDTO(_startDate.value!!, _endDate.value!!)
                _totalRevenue.value = adminRepository.getTotalRevenueFromDateToDate(request)
                _totalPenalty.value = adminRepository.getTotalPenaltyFromDateToDate(request)
            } else {
                _totalRevenue.value = adminRepository.getTotalRevenue()
                _totalPenalty.value = adminRepository.getTotalPenalty()
            }
        }
    }

    private fun loadContractSummary() {
        viewModelScope.launch {
            _contractSummary.value = if (_startDate.value != null && _endDate.value != null) {
                adminRepository.getContractSummaryFromDateToDate(
                    ReportRequestDTO(_startDate.value!!, _endDate.value!!)
                )
            } else {
                adminRepository.getContractSummary()
            }
        }
    }

    private fun loadReturnStatus() {
        viewModelScope.launch {
            _returnStatus.value = if (_startDate.value != null && _endDate.value != null) {
                adminRepository.getReturnStatusFromDateToDate(
                    ReportRequestDTO(_startDate.value!!, _endDate.value!!)
                )
            } else {
                adminRepository.getReturnStatus()
            }
        }
    }

    private fun loadTop3Cars() {
        viewModelScope.launch {
            _top3Cars.value = if (_startDate.value != null && _endDate.value != null) {
                val request = ReportRequestDTO(_startDate.value!!, _endDate.value!!)
                when (_carFilterOption.value) {
                    "Rental Count" -> adminRepository.getTop3RentedCarsFromDateToDate(request)
                    else -> adminRepository.getTop3RatingCarsFromDateToDate(request)
                }
            } else {
                when (_carFilterOption.value) {
                    "Rental Count" -> adminRepository.getTop3RentedCars()
                    else -> adminRepository.getTop3RatingCars()
                }
            }
        }
    }

    private fun loadTop3Users() {
        viewModelScope.launch {
            if (_startDate.value != null && _endDate.value != null) {
                val request = ReportRequestDTO(_startDate.value!!, _endDate.value!!)
                _top3BestUsers.value = adminRepository.getTop3BestUsersFromDateToDate(request)
                _top3WorstUsers.value = adminRepository.getTop3WorstUsersFromDateToDate(request)
            } else {
                _top3BestUsers.value = adminRepository.getTop3BestUsers()
                _top3WorstUsers.value = adminRepository.getTop3WorstUsers()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val adminRepository = (this[APPLICATION_KEY] as CarRentalApplication).container.adminRepository
                AnalyticsViewModel(adminRepository)
            }
        }
    }
}