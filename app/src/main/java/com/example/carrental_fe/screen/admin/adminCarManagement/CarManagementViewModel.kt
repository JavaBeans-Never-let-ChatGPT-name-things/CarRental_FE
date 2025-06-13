package com.example.carrental_fe.screen.admin.adminCarManagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AdminRepository
import com.example.carrental_fe.data.CarRepository
import com.example.carrental_fe.dto.request.CarPageRequestDTO
import com.example.carrental_fe.model.Car
import com.example.carrental_fe.model.CarBrand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class CarManagementViewModel (private val carRepository: CarRepository,
                              private val adminRepository: AdminRepository): ViewModel() {
    private val _carBrands = MutableStateFlow<List<CarBrand>>(emptyList())
    val carBrands: StateFlow<List<CarBrand>> = _carBrands

    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val carList: StateFlow<List<Car>> = _cars

    private val _currentPage = MutableStateFlow<Int>(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _totalPages = MutableStateFlow<Int>(1)
    val totalPages: StateFlow<Int> = _totalPages

    private val _selectedBrand = MutableStateFlow<CarBrand?>(null)
    val selectedBrand: StateFlow<CarBrand?> = _selectedBrand

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoadingBrand = MutableStateFlow(false)
    val isLoadingBrand: StateFlow<Boolean> = _isLoadingBrand

    init {
        loadCarBrands()
        loadCars()
    }
    fun loadCarBrands() {
        viewModelScope.launch {
            _isLoadingBrand.value = true
            val response = carRepository.getCarBrands()
            if (response.isSuccessful) {
                _carBrands.value = response.body() ?: emptyList()
            }
            val count = carRepository.getCount().body() ?: 1L
            _totalPages.value = ((count + 9) / 10).toInt()
            _isLoadingBrand.value = false
        }
    }
    fun addBrand (name: String, logo: File)
    {
        viewModelScope.launch {
            try {
                val result = adminRepository.addCarBrand(name, logo).message
                if (result != "Car brand added successfully"){
                    _error.value = result
                }
                loadCarBrands()
            }
            catch (e: Exception)
            {

                _error.value = e.message ?: "Unknown error"
            }
        }
    }
    fun addCar (
        id: String,
        brandName: String,
        maxSpeed: Float,
        carRange: Float,
        carImage: File?,
        seatsNumber: Int,
        rentalPrice: Float,
        engineType: String,
        gearType: String,
        drive: String
    )
    {
        viewModelScope.launch {
            try {
                val response = adminRepository.addCar(
                    id,
                    brandName,
                    maxSpeed,
                    carRange,
                    carImage,
                    seatsNumber,
                    rentalPrice,
                    engineType,
                    gearType,
                    drive
                ).message
                if (response != "Successfully added car $id"){
                    _error.value = response
                }
                loadCars()
            }
            catch (e: Exception)
            {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
    fun loadCarsByBrand(brand: CarBrand)
    {
        _selectedBrand.value = brand
        viewModelScope.launch {
            _isLoading.value = true
            val count = carRepository.getCountByBrand(brand.id).body() ?: 1L
            _totalPages.value = ((count + 9) / 10).toInt()
            _currentPage.value = 1
            val request = CarPageRequestDTO(
                pageNo = currentPage.value - 1,
                sort = "ASC",
                sortByColumn = "id"
            )
            val response = carRepository.getCarPageByBrand(request, brand.id)
            if (response.isSuccessful) {
                _cars.value = response.body() ?: emptyList()
            }
            _isLoading.value = false
        }
    }
    fun loadCars() {
        viewModelScope.launch {
            _isLoading.value = true
            val request = CarPageRequestDTO(
                pageNo = currentPage.value - 1,
                sort = "ASC",
                sortByColumn = "id"
            )
            val response = carRepository.getCarPage(request)
            if (response.isSuccessful) {
                _cars.value = response.body() ?: emptyList()
            }
            _isLoading.value = false
        }
    }
    fun goToPage(page: Int) {
        if (page in 1..totalPages.value) {
            _currentPage.value = page
            if (selectedBrand.value != null) {
                val request = CarPageRequestDTO(
                    pageNo = currentPage.value - 1,
                    sort = "ASC",
                    sortByColumn = "id"
                )
                viewModelScope.launch {
                    _isLoading.value = true
                    val response = carRepository.getCarPageByBrand(request, selectedBrand.value!!.id)
                    if (response.isSuccessful) {
                        _cars.value = response.body() ?: emptyList()
                    }
                    _isLoading.value = false
                }
            } else loadCars()
        }
    }fun resetPage(){
        _currentPage.value = 1
        _selectedBrand.value = null
        viewModelScope.launch {
            val count = carRepository.getCount().body() ?: 1L
            _totalPages.value = ((count + 9) / 10).toInt()
        }
        loadCars()
    }
    fun nextPage() = goToPage(currentPage.value + 1)
    fun prevPage() = goToPage(currentPage.value - 1)

    fun clearError() {
        _error.value = null
    }
    fun setError(message: String) {
        _error.value = message
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val carRepository = application.container.carRepository
                val adminRepository = application.container.adminRepository
                CarManagementViewModel(carRepository, adminRepository)
            }
        }
    }
}

