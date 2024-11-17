package com.example.itcc4finalproject.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itcc4finalproject.Domain.AppointmentModel
import com.example.itcc4finalproject.Domain.CategoryModel
import com.example.itcc4finalproject.Domain.UserModel
import com.example.itcc4finalproject.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _currentUser = MutableStateFlow<UserModel?>(null)
    val currentUser: StateFlow<UserModel?> = _currentUser

    private val _appointments = MutableStateFlow<List<AppointmentModel>>(emptyList())
    val appointments: StateFlow<List<AppointmentModel>> = _appointments

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadCurrentUser()
        loadCategories()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            repository.getCurrentUserId()?.let { userId ->
                repository.getUser(userId).fold(
                    onSuccess = { user ->
                        _currentUser.value = user
                        loadUserAppointments(userId)
                    },
                    onFailure = { _error.value = it.message }
                )
            }
        }
    }

    private fun loadUserAppointments(userId: String) {
        viewModelScope.launch {
            repository.getAppointmentsForUser(userId).collect { appointmentList ->
                _appointments.value = appointmentList
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().fold(
                onSuccess = { _categories.value = it },
                onFailure = { _error.value = it.message }
            )
        }
    }

    fun createAppointment(appointment: AppointmentModel) {
        viewModelScope.launch {
            repository.createAppointment(appointment).fold(
                onSuccess = { loadUserAppointments(appointment.patientId) },
                onFailure = { _error.value = it.message }
            )
        }
    }

    fun updateAppointmentStatus(appointmentId: String, status: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointmentId, status).fold(
                onSuccess = { loadCurrentUser() },
                onFailure = { _error.value = it.message }
            )
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            repository.signIn(email, password).fold(
                onSuccess = { loadCurrentUser() },
                onFailure = { _error.value = it.message }
            )
        }
    }

    fun signUp(email: String, password: String, user: UserModel) {
        viewModelScope.launch {
            repository.signUp(email, password).fold(
                onSuccess = { userId ->
                    repository.createUser(user.copy(id = userId)).fold(
                        onSuccess = { loadCurrentUser() },
                        onFailure = { _error.value = it.message }
                    )
                },
                onFailure = { _error.value = it.message }
            )
        }
    }

    fun signOut() {
        repository.signOut()
        _currentUser.value = null
        _appointments.value = emptyList()
    }
}
