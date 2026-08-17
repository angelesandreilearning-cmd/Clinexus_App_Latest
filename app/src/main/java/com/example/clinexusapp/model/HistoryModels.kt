package com.example.clinexusapp.model

import com.google.gson.annotations.SerializedName

data class AppointmentsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("appointments") val appointments: List<AppointmentDTO>
)

data class AppointmentDTO(
    @SerializedName("appointment_id") val id: Int,
    @SerializedName("patient_id") val patientId: Int?,
    @SerializedName("patient_name") val patientName: String?,
    @SerializedName("dentist_id") val dentistId: Int?,
    @SerializedName("dentist_name") val dentistName: String?,
    @SerializedName("appointment_date") val date: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String,
    @SerializedName("appointment_type") val type: String,
    @SerializedName("appointment_status") val status: String,
    @SerializedName("notes") val notes: String?,
    @SerializedName("cancellation_note") val cancellationNote: String?,
    @SerializedName("billing_id") val billingId: Int?
) {
    val doctor: String get() = dentistName ?: "Unknown Dentist"
    val treatment: String get() = type.replaceFirstChar { it.uppercase() }
}

data class ChatMessageDTO(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long,
    val isImage: Boolean = false
)

data class ClinicNewsDTO(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String
)

data class HealthInsightDTO(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("iconEmoji") val iconEmoji: String
)
