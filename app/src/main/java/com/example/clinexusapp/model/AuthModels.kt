package com.example.clinexusapp.model

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class ForgotPasswordRequest(
    val email: String
)

data class ResetPasswordRequest(
    val email: String,
    val otp: String,
    val newPassword: String
)

data class GenericResponse(
    val success: Boolean,
    val message: String
)

data class UpdateProfileRequest(
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val phoneNumber: String,
    val dateOfBirth: String,
    val streetAddress: String,
    val province: String,
    val city: String,
    val barangay: String
)
