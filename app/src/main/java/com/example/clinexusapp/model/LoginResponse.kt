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
    val middleName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val dateOfBirth: String? = null,
    val streetAddress: String? = null,
    val province: String? = null,
    val city: String? = null,
    val barangay: String? = null
)
