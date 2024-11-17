package com.example.itcc4finalproject.Repository

import com.example.itcc4finalproject.Domain.AppointmentModel
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val appointmentCollection = firestore.collection("appointments")

    fun createAppointment(appointment: AppointmentModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        appointmentCollection.document()
            .set(appointment)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
