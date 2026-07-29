package com.example.clinexusapp.model

data class LoginResponse(
    val message: String? = null,
    val token: String? = null,
    val patient: PatientInfo? = null,
    val success: Boolean? = null
)

data class PatientInfo(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String
)
