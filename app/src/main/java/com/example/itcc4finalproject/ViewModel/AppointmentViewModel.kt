package com.example.itcc4finalproject.ViewModel

import androidx.lifecycle.ViewModel
import com.example.itcc4finalproject.Domain.AppointmentModel
import com.example.itcc4finalproject.Repository.AppointmentRepository

class AppointmentViewModel : ViewModel() {
    private val repository = AppointmentRepository()

    fun createAppointment(
        appointment: AppointmentModel,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.createAppointment(appointment, onSuccess, onFailure)
    }
}
