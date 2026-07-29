package com.example.clinexusapp.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val phoneNumber: String,
    val dateOfBirth: String,
    val sex: String
)
