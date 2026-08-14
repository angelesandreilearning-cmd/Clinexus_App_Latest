package com.example.clinexusapp.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val message: String? = null,
    val token: String? = null,
    val patient: PatientInfo? = null,
    val success: Boolean? = null
)

data class PatientInfo(
    @SerializedName("accountID", alternate = ["account_id"]) val accountID: Int,
    @SerializedName("patientID", alternate = ["patient_id"]) val patientID: Int,
    val role: String,
    val email: String? = null,
    @SerializedName("firstName", alternate = ["first_name"]) val firstName: String? = null,
    @SerializedName("middleName", alternate = ["middle_name"]) val middleName: String? = null,
    @SerializedName("lastName", alternate = ["last_name"]) val lastName: String? = null,
    @SerializedName("phoneNumber", alternate = ["phone_number"]) val phoneNumber: String? = null,
    @SerializedName("dateOfBirth", alternate = ["date_of_birth"]) val dateOfBirth: String? = null,
    @SerializedName("streetAddress", alternate = ["street_address"]) val streetAddress: String? = null,
    @SerializedName("province") val province: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("barangay") val barangay: String? = null
)
