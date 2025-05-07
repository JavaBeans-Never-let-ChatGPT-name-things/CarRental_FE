package com.example.carrental_fe.screen.user.userNotificationScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.NotificationRepository
import com.example.carrental_fe.model.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications = _notifications.asStateFlow()

    private val _hasEnteredScreen = MutableStateFlow(false)
    val hasEnteredScreen = _hasEnteredScreen.asStateFlow()

    val unreadNotificationCount = notifications.map { list -> list.count { !it.isRead } }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), 0
    )

    init {
        fetchNotifications()
    }

    fun fetchNotifications() {
        viewModelScope.launch {
            _notifications.value = repository.getNotifications()
        }
    }

    fun markEnteredScreen() {
        _hasEnteredScreen.value = true
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            repository.readAllNotifications()
            _notifications.update { list -> list.map { it.copy(isRead = true) } }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as CarRentalApplication
                val notificationRepository = app.container.notificationRepository
                NotificationViewModel(notificationRepository)
            }
        }
    }
}