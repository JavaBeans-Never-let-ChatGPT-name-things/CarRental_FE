package com.example.carrental_fe.screen.admin.adminUserList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.data.AdminRepository
import com.example.carrental_fe.dto.response.UserDTO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class UserListViewModel(private val adminRepository: AdminRepository): ViewModel() {

    private val _users = MutableStateFlow<List<UserDTO>>(emptyList())
    val users: StateFlow<List<UserDTO>> = _users

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _selectedSort = MutableStateFlow("Select an option")
    val selectedSort: StateFlow<String> = _selectedSort

    private val _selectedStatus = MutableStateFlow("Total User")
    val selectedStatus: StateFlow<String> = _selectedStatus

    private val _initialStats = MutableStateFlow<Triple<Int, Int, Int>?>(null)
    val initialStats: StateFlow<Triple<Int, Int, Int>?> = _initialStats

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var initialized = false

    init {
        observeQuerySortAndStatus()
    }

    @OptIn(FlowPreview::class)
    private fun observeQuerySortAndStatus() {
        viewModelScope.launch {
            combine(
                _query.debounce(500).distinctUntilChanged(),
                _selectedSort,
                _selectedStatus
            ) { query, sort, status -> Triple(query, sort, status) }
                .collectLatest { (queryText, sort, status) ->
                    fetchUsers(queryText, sort, status)
                }
        }
    }

    private fun fetchUsers(query: String, sort: String, status: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = adminRepository.getUserList(query, sort, status)
            _users.value = result
            _isLoading.value = false
            if (!initialized) {
                _initialStats.value = Triple(
                    result.size,
                    result.count { it.enabled },
                    result.count { !it.enabled }
                )
                initialized = true
            }
        }
    }

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun updateSort(sort: String) {
        _selectedSort.value = sort
    }

    fun updateStatus(status: String) {
        _selectedStatus.value = status
    }

    fun promoteUser(userName: String)
    {
        viewModelScope.launch {
            try{
                adminRepository.promoteUser(userName)
                fetchUsers(_query.value, _selectedSort.value, _selectedStatus.value)
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun demoteUser(userName: String)
    {
        viewModelScope.launch {
            try{
                adminRepository.demoteUser(userName)
                fetchUsers(_query.value, _selectedSort.value, _selectedStatus.value)
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as CarRentalApplication
                UserListViewModel(app.container.adminRepository)
            }
        }
    }
}