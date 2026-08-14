package com.example.clinexusapp.api

import com.example.clinexusapp.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/patient-login")
    suspend fun loginPatient(
        @Body request: LoginRequest,
    ): Response<LoginResponse>

    @POST("api/patient-register")
    suspend fun registerPatient(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("api/verify-email")
    suspend fun verifyEmail(
        @Body request: VerifyOtpRequest
    ): Response<GenericResponse>

    @POST("api/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<GenericResponse>

    @POST("api/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<GenericResponse>

    @POST("api/appointments")
    suspend fun createAppointment(
        @Header("Authorization") token: String,
        @Body request: CreateAppointmentRequest
    ): Response<CreateAppointmentResponse>

    @GET("api/patient-history")
    suspend fun getAppointmentHistory(
        @Header("Authorization") token: String
    ): Response<List<AppointmentDTO>>

    @GET("api/patient-profile")
    suspend fun getPatientProfile(
        @Header("Authorization") token: String
    ): Response<PatientInfo>

    @PUT("api/patient-profile")
    suspend fun updatePatientProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<GenericResponse>

    @GET("api/available-contacts")
    suspend fun getAvailableContacts(
        @Header("Authorization") token: String
    ): Response<List<ContactDTO>>

    @GET("api/conversations")
    suspend fun getConversations(
        @Header("Authorization") token: String
    ): Response<List<ConversationDTO>>

    @GET("api/conversations/{conversationID}/messages")
    suspend fun getConversationMessages(
        @Header("Authorization") token: String,
        @Path("conversationID") conversationID: Int
    ): Response<ConversationMessagesResponse>

    @POST("api/conversations/send-message")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body request: SendMessageRequest
    ): Response<SendMessageResponse>

    @PATCH("api/conversations/{conversationID}/read")
    suspend fun markConversationAsRead(
        @Header("Authorization") token: String,
        @Path("conversationID") conversationID: Int,
        @Body request: MarkReadRequest
    ): Response<GenericResponse>

    @GET("api/chat-messages")
    suspend fun getChatMessages(
        @Header("Authorization") token: String
    ): Response<List<ChatMessageDTO>>

    @PUT("api/appointments/{id}")
    suspend fun updateAppointment(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): Response<Unit>

    @DELETE("api/appointments/{id}")
    suspend fun deleteAppointment(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<Unit>

    @GET("api/clinic-news")
    suspend fun getClinicNews(
        @Header("Authorization") token: String
    ): Response<List<ClinicNewsDTO>>

    @GET("api/health-insights")
    suspend fun getHealthInsights(
        @Header("Authorization") token: String
    ): Response<List<HealthInsightDTO>>


}
