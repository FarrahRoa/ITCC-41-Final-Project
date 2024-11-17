package com.example.itcc4finalproject.Repository

import com.example.itcc4finalproject.Domain.AppointmentModel
import com.example.itcc4finalproject.Domain.CategoryModel
import com.example.itcc4finalproject.Domain.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FirebaseRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // User Management
    suspend fun createUser(user: UserModel): Result<UserModel> {
        return try {
            firestore.collection("users")
                .document(user.id)
                .set(user)
                .await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(userId: String): Result<UserModel> {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            val user = document.toObject(UserModel::class.java)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Appointment Management
    suspend fun createAppointment(appointment: AppointmentModel): Result<AppointmentModel> {
        return try {
            val docRef = firestore.collection("appointments").document()
            val appointmentWithId = appointment.copy(id = docRef.id)
            docRef.set(appointmentWithId).await()
            Result.success(appointmentWithId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAppointmentsForUser(userId: String): Flow<List<AppointmentModel>> = flow {
        try {
            val snapshot = firestore.collection("appointments")
                .whereEqualTo("patientId", userId)
                .get()
                .await()
            val appointments = snapshot.toObjects(AppointmentModel::class.java)
            emit(appointments)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    suspend fun updateAppointmentStatus(appointmentId: String, status: String): Result<Unit> {
        return try {
            firestore.collection("appointments")
                .document(appointmentId)
                .update("status", status)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Category Management
    suspend fun getCategories(): Result<List<CategoryModel>> {
        return try {
            val snapshot = firestore.collection("categories")
                .get()
                .await()
            Result.success(snapshot.toObjects(CategoryModel::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Authentication
    suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: throw Exception("Authentication failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: throw Exception("User creation failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
