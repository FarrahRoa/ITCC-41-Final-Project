package com.example.itcc4finalproject.Domain

data class AppointmentModel(
    val id: String = "",
    val patientId: String = "",
    val doctorId: String = "",
    val date: String = "",
    val time: String = "",
    val status: String = "Pending"
)
