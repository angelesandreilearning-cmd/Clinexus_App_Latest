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
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "An unknown error occurred")
            }
        } catch (e: IOException) {
            Resource.Error("Could not connect to server. Check your internet connection.")
        } catch (e: HttpException) {
            Resource.Error("Server returned an error. Please try again later.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun register(request: RegisterRequest): Resource<RegisterResponse> {
        return try {
            val response = apiService.registerPatient(request)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "An unknown error occurred")
            }
        } catch (e: IOException) {
            Resource.Error("Could not connect to server. Check your internet connection.")
        } catch (e: HttpException) {
            Resource.Error("Server returned an error. Please try again later.")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun getAppointmentHistory(): Resource<List<AppointmentDTO>> {
        return try {
            val token = SessionManager.token ?: return Resource.Error("Not authenticated")
            val response = apiService.getAppointmentHistory("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
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
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to fetch messages")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }
}
