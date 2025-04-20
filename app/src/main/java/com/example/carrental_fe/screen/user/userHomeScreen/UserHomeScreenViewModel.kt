package com.example.carrental_fe.screen.user.userHomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.CarRepository
import com.example.carrental_fe.dto.request.CarPageRequestDTO
import com.example.carrental_fe.model.Car
import com.example.carrental_fe.model.CarBrand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserHomeScreenViewModel (private val carRepository: CarRepository) : ViewModel() {

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

    private val _favouriteCars = MutableStateFlow<List<Car>>(emptyList())
    val favouriteCars: StateFlow<List<Car>> = _favouriteCars

    init {
        loadCarBrands()
        loadCars()
        loadFavourites()
    }
    fun loadCarBrands(){
        viewModelScope.launch {
            val response = carRepository.getCarBrands()
            if (response.isSuccessful) {
                _carBrands.value = response.body() ?: emptyList()
            }
            val count = carRepository.getCount().body() ?: 1L
            _totalPages.value = ((count +9) /10).toInt()
        }
    }
    fun loadCarsByBrand(brand: CarBrand)
    {
        _selectedBrand.value = brand
        viewModelScope.launch {
            val count = carRepository.getCountByBrand(brand.id).body() ?: 1L
            _totalPages.value = ((count + 9) / 10).toInt()
            _currentPage.value = 1
            val request = CarPageRequestDTO(
                pageNo = currentPage.value - 1,
                sort = "ASC",
                sortByColumn = "id"
            )
            val response = carRepository.getCarPageByBrand(request, brand.id)
            if (response.isSuccessful){
                _cars.value = response.body() ?: emptyList()
            }
        }
    }
    fun loadCars() {
        viewModelScope.launch {
            val request = CarPageRequestDTO(
                pageNo = currentPage.value - 1,
                sort = "ASC",
                sortByColumn = "id"
            )
            val response = carRepository.getCarPage(request)
            if (response.isSuccessful) {
                _cars.value = response.body() ?: emptyList()
            }
        }
    }
    fun goToPage(page : Int){
        if(page in 1..totalPages.value){
            _currentPage.value = page
            if(selectedBrand.value != null){
                val request = CarPageRequestDTO(
                    pageNo = currentPage.value -1,
                    sort = "ASC",
                    sortByColumn = "id"
                )
                viewModelScope.launch {
                    val response = carRepository.getCarPageByBrand(request,selectedBrand.value!!.id)
                    if(response.isSuccessful){
                        _cars.value = response.body() ?: emptyList()
                    }
                }
            } else loadCars()
        }
    }
    fun resetPage(){
        _currentPage.value =1
        _selectedBrand.value = null
        viewModelScope.launch {
            val count = carRepository.getCount().body() ?: 1L
            _totalPages.value = ((count +9) /10).toInt()
        }
        loadCars()
    }
    fun toggleFavourite(carId : String){
        viewModelScope.launch {
            try{
                val response = carRepository.updateFavourite(carId)
                val currentFavourites = _favouriteCars.value.toMutableList()
                val index = currentFavourites.indexOfFirst { it.id == carId }
                if (response.message == "Favourite car updated successfully")
                {
                    if(index >= 0 ){
                        currentFavourites.removeAt(index)
                    } else {
                        val car = _cars.value.find{ it.id == carId}
                        if (car != null) {
                            currentFavourites.add(car)
                        }
                    }
                    _favouriteCars.value = currentFavourites

                }
            } catch (e : Exception){
            }
        }
    }
    fun loadFavourites() {
        viewModelScope.launch {
            try{
                val response = carRepository.getFavourites()
                if(response.isSuccessful){
                    _favouriteCars.value = response.body() ?: emptyList()
                }
            } catch(e: Exception){
            }
        }
    }
    private var shouldReset = false

    fun storeHomeScreenState() {
        shouldReset = true
    }

    fun shouldResetPage(): Boolean {
        return if (shouldReset) {
            shouldReset = false
            true
        } else false
    }
    fun nextPage() = goToPage(currentPage.value + 1)
    fun prevPage() = goToPage(currentPage.value - 1)
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val carRepository = application.container.carRepository
                UserHomeScreenViewModel(carRepository = carRepository)
            }
        }
    }
}
