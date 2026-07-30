package com.example.clinexusapp.model

data class AppointmentDTO(
    val id: String,
    val doctor: String,
    val date: String,
    val time: String,
    val status: String,
    val treatment: String
)

data class ChatMessageDTO(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long,
    val isImage: Boolean = false
)

data class ClinicNewsDTO(
    val id: String,
    val title: String,
    val description: String,
    val date: String
)

data class HealthInsightDTO(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val iconEmoji: String
)
