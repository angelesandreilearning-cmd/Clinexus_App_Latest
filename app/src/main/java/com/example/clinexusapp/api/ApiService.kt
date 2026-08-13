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

    @POST("api/verify-otp")
    suspend fun verifyOtp(
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
