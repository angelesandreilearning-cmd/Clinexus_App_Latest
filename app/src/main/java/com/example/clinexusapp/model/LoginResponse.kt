package com.example.clinexusapp.model

data class LoginResponse(
    val message: String? = null,
    val token: String? = null,
    val patient: PatientInfo? = null,
    val success: Boolean? = null
)

data class PatientInfo(
    val accountID: Int,
    val patientID: Int,
    val role: String,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null
)
