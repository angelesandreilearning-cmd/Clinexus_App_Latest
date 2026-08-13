package com.example.clinexusapp.api

import com.example.clinexusapp.model.*
import com.example.clinexusapp.util.Resource
import com.example.clinexusapp.util.SessionManager
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(private val apiService: ApiService) {

    suspend fun login(request: LoginRequest): Resource<LoginResponse> {
        return try {
            val response = apiService.loginPatient(request)
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "An unknown error occurred")
            }
        } catch (e: IOException) {
            Resource.Error("Could not connect to server. Check your internet connection.")
        } catch (_: HttpException) {
            Resource.Error("Server returned an error. Please try again later.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun register(request: RegisterRequest): Resource<RegisterResponse> {
        return try {
            val response = apiService.registerPatient(request)
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "An unknown error occurred")
            }
        } catch (e: IOException) {
            Resource.Error("Could not connect to server. Check your internet connection.")
        } catch (_: HttpException) {
            Resource.Error("Server returned an error. Please try again later.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun verifyOtp(email: String, otp: String): Resource<GenericResponse> {
        return try {
            val request = VerifyOtpRequest(email, otp)
            val response = apiService.verifyOtp(request)
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "OTP verification failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun forgotPassword(email: String): Resource<GenericResponse> {
        return try {
            val request = ForgotPasswordRequest(email)
            val response = apiService.forgotPassword(request)
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Password reset request failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun resetPassword(email: String, otp: String, newPassword: String): Resource<GenericResponse> {
        return try {
            val request = ResetPasswordRequest(email, otp, newPassword)
            val response = apiService.resetPassword(request)
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Password reset failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun createAppointment(serviceId: String, doctorName: String, date: String, time: String): Resource<CreateAppointmentResponse> {
        return try {
            val token = SessionManager.token ?: return Resource.Error("Not authenticated")
            val request = CreateAppointmentRequest(serviceId, doctorName, date, time)
            val response = apiService.createAppointment("Bearer $token", request)
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to create appointment")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun getAppointmentHistory(): Resource<List<AppointmentDTO>> {
        return try {
            val token = SessionManager.token ?: return Resource.Error("Not authenticated")
            val response = apiService.getAppointmentHistory("Bearer $token")
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to fetch history")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun getChatMessages(): Resource<List<ChatMessageDTO>> {
        return try {
            val token = SessionManager.token ?: return Resource.Error("Not authenticated")
            val response = apiService.getChatMessages("Bearer $token")
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to fetch messages")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun updateAppointment(id: String, updates: Map<String, String>): Resource<Unit> {
        return try {
            val token = SessionManager.token ?: return Resource.Error("Not authenticated")
            val response = apiService.updateAppointment(id, "Bearer $token", updates)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Update failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun cancelAppointment(id: String): Resource<Unit> {
        return try {
            val token = SessionManager.token ?: return Resource.Error("Not authenticated")
            val response = apiService.deleteAppointment(id, "Bearer $token")
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Cancellation failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun getClinicNews(): Resource<List<ClinicNewsDTO>> {
        return try {
            val token = SessionManager.token ?: return Resource.Error("Not authenticated")
            val response = apiService.getClinicNews("Bearer $token")
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to fetch news")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun getHealthInsights(): Resource<List<HealthInsightDTO>> {
        return try {
            val token = SessionManager.token ?: return Resource.Error("Not authenticated")
            val response = apiService.getHealthInsights("Bearer $token")
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to fetch insights")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }
}
