package com.example.clinexusapp.model

data class CreateAppointmentRequest(
    val serviceId: String,
    val doctorName: String,
    val date: String,
    val time: String
)

data class CreateAppointmentResponse(
    val success: Boolean,
    val message: String,
    val appointmentId: String? = null
)
