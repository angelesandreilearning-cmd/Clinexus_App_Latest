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

    suspend fun verifyEmail(email: String, otp: String): Resource<GenericResponse> {
        return try {
            val request = VerifyOtpRequest(email, otp)
            val response = apiService.verifyEmail(request)
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

    suspend fun getPatientProfile(): Resource<PatientInfo> {
        return try {
            val token = SessionManager.token ?: return Resource.Error("Not authenticated")
            val response = apiService.getPatientProfile("Bearer $token")
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to fetch profile")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun updatePatientProfile(request: UpdateProfileRequest): Resource<GenericResponse> {
        return try {
            val token = SessionManager.token ?: return Resource.Error("Not authenticated")
            val response = apiService.updatePatientProfile("Bearer $token", request)
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to update profile")
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

    suspend fun getAvailableContacts(): Resource<List<ContactDTO>> {
        return try {
            val token = SessionManager.token ?: return Resource.Error<List<ContactDTO>>("Not authenticated")
            val response = apiService.getAvailableContacts("Bearer $token")
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to fetch contacts")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun getConversations(): Resource<List<ConversationDTO>> {
        return try {
            val token = SessionManager.token ?: return Resource.Error<List<ConversationDTO>>("Not authenticated")
            val response = apiService.getConversations("Bearer $token")
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to fetch conversations")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun getConversationMessages(conversationID: Int): Resource<ConversationMessagesResponse> {
        return try {
            val token = SessionManager.token ?: return Resource.Error<ConversationMessagesResponse>("Not authenticated")
            val response = apiService.getConversationMessages("Bearer $token", conversationID)
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to fetch messages")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun sendMessage(receiverAccountType: String, receiverAccountID: Int, messageContent: String): Resource<SendMessageResponse> {
        return try {
            val token = SessionManager.token ?: return Resource.Error<SendMessageResponse>("Not authenticated")
            val request = SendMessageRequest(receiverAccountType, receiverAccountID, messageContent)
            val response = apiService.sendMessage("Bearer $token", request)
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to send message")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun markConversationAsRead(conversationID: Int, lastReadMessageID: Int): Resource<GenericResponse> {
        return try {
            val token = SessionManager.token ?: return Resource.Error<GenericResponse>("Not authenticated")
            val request = MarkReadRequest(lastReadMessageID)
            val response = apiService.markConversationAsRead("Bearer $token", conversationID, request)
            if (response.isSuccessful && (response.body() != null)) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to mark as read")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }
}
