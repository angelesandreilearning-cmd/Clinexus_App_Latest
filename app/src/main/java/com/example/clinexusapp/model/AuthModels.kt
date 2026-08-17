package com.example.clinexusapp.model

import com.google.gson.annotations.SerializedName

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
    @SerializedName("first_name") val firstName: String,
    @SerializedName("middle_name") val middleName: String?,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("date_of_birth") val dateOfBirth: String,
    @SerializedName("street_address") val streetAddress: String,
    @SerializedName("province") val province: String,
    @SerializedName("city") val city: String,
    @SerializedName("barangay") val barangay: String
)
