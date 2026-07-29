package com.example.clinexusapp.api

import com.example.clinexusapp.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/patient-login")
    suspend fun loginPatient(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/patient-register")
    suspend fun registerPatient(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @GET("api/patient-history")
    suspend fun getAppointmentHistory(
        @Header("Authorization") token: String
    ): Response<List<AppointmentDTO>>

    @GET("api/chat-messages")
    suspend fun getChatMessages(
        @Header("Authorization") token: String
    ): Response<List<ChatMessageDTO>>
}
