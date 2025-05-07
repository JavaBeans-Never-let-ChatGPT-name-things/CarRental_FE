package com.example.carrental_fe.screen.user.userFavScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.CarRepository
import com.example.carrental_fe.model.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavouriteScreenViewModel (private val carRepository: CarRepository) : ViewModel() {
    private val _favouriteCars = MutableStateFlow<List<Car>>(emptyList())
    val favouriteCars: StateFlow<List<Car>> = _favouriteCars

    private val _currentList = MutableStateFlow<List<Car>>(emptyList())
    val currentList: StateFlow<List<Car>> = _currentList

    fun resetFavourite(){
        viewModelScope.launch {
            val responseFav = carRepository.getFavourites()
            if (responseFav.isSuccessful) {
                _favouriteCars.value = responseFav.body() ?: emptyList()
                _currentList.value = _favouriteCars.value
            }
        }
    }
    fun toggleFavouriteInFavScreen(carId: String) {
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
                        val car = _currentList.value.find { it.id == carId }
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
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarRentalApplication)
                val carRepository = application.container.carRepository
                FavouriteScreenViewModel(carRepository = carRepository)
            }
        }
    }
}