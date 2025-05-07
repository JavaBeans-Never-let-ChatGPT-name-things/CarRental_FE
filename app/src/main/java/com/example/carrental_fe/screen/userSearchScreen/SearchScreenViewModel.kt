package com.example.carrental_fe.screen.userSearchScreen

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchScreenViewModel (private val carRepository: CarRepository) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val carList: StateFlow<List<Car>> = _cars

    private val _currentPage = MutableStateFlow<Int>(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _totalPages = MutableStateFlow<Int>(1)
    val totalPages: StateFlow<Int> = _totalPages

    private val _favouriteCars = MutableStateFlow<List<Car>>(emptyList())
    val favouriteCars: StateFlow<List<Car>> = _favouriteCars
    fun resetSearchScreen(){
        _query.value = ""
        _currentPage.value = 1
        _totalPages.value = 0
        _cars.value = emptyList()
        loadFavouriteCar()
    }
    fun setCurrentPage() {
        _currentPage.value = 1
    }
    fun setSearchQuery(query: String) {
        _query.value = query
    }
    fun loadFavouriteCar(){
        viewModelScope.launch {
            val response = carRepository.getFavourites()
            if (response.isSuccessful) {
                _favouriteCars.value = response.body() ?: emptyList()
            }
        }
    }
    fun getCars(){
        if (query.value.isEmpty())
        {
            _cars.value = emptyList()
            _totalPages.value = 0
            return
        }
        viewModelScope.launch {
            val request = CarPageRequestDTO(
                pageNo = currentPage.value - 1,
                sort = "ASC",
                sortByColumn = "id"
            )
            val count = carRepository.countFilter(_query.value).body()?: 0L
            _totalPages.value = ((count + 9) / 10).toInt()
            val response = carRepository.getFilterCarPageByBrand(request, query.value)
            if (response.isSuccessful) {
                _cars.value = response.body() ?: emptyList()
            }
            else
            {
                _cars.value = emptyList()
            }

        }
    }
    fun toggleFavourite(carId: String) {
        viewModelScope.launch {
            try {
                val response = carRepository.updateFavourite(carId)
                val currentFavourites = _favouriteCars.value.toMutableList()
                val index = currentFavourites.indexOfFirst { it.id == carId }
                if (response.message == "Favourite car updated successfully")
                {
                    if (index >= 0) {
                        currentFavourites.removeAt(index)
                    } else {
                        val car = _cars.value.find { it.id == carId }
                        if (car != null) {
                            currentFavourites.add(car)
                        }
                    }
                    _favouriteCars.value = currentFavourites

                }
            } catch (e: Exception) {
            }
        }
    }
    fun nextSearchPage() = gotoSearchPage(currentPage.value + 1)
    fun prevSearchPage() = gotoSearchPage(currentPage.value - 1)
    fun gotoSearchPage(page: Int) {
        if (page in 1..totalPages.value) {
            _currentPage.value = page
            getCars()
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val carRepository = application.container.carRepository
                SearchScreenViewModel(carRepository = carRepository)
            }
        }
    }
}