package com.example.clinexusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinexusapp.api.AuthRepository
import com.example.clinexusapp.data.model.Booking
import com.example.clinexusapp.data.model.DentalService
import com.example.clinexusapp.data.model.TimeSlot
import com.example.clinexusapp.model.CreateAppointmentResponse
import com.example.clinexusapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class BookingViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _bookingState = MutableStateFlow<Resource<CreateAppointmentResponse>?>(null)
    val bookingState = _bookingState.asStateFlow()

    private val _services = MutableStateFlow(listOf(
        DentalService("1", "Consultation", 30, "$50", emoji = "⚕️"),
        DentalService("2", "Deep Cleaning", 45, "$85", emoji = "✨"),
        DentalService("3", "Braces Checkup", 60, "$120", emoji = "🦷"),
        DentalService("4", "Extraction", 60, "$150", emoji = "🏥")
    ))
    val services = _services.asStateFlow()

    private val _selectedService = MutableStateFlow<DentalService?>(null)
    val selectedService = _selectedService.asStateFlow()

    private val _timeSlots = MutableStateFlow<List<TimeSlot>>(emptyList())
    val timeSlots = _timeSlots.asStateFlow()

    // Simulated existing bookings (representing other patients)
    private val existingBookings = listOf(
        Booking("b1", "3", "09:00 AM", 60, 1), // Dentist 1 busy 9-10 AM
        Booking("b2", "1", "09:00 AM", 30, 2), // Dentist 2 busy 9-9:30 AM
        Booking("b3", "2", "10:30 AM", 45, 1), // Dentist 1 busy 10:30-11:15 AM
        Booking("b4", "4", "01:00 PM", 60, 1), // Dentist 1 busy 1-2 PM
        Booking("b5", "3", "01:00 PM", 60, 2)  // Dentist 2 busy 1-2 PM -> 01:00 PM is FULL
    )

    init {
        generateSlots()
    }

    fun selectService(service: DentalService) {
        _selectedService.value = service
        generateSlots()
    }

    private fun generateSlots() {
        val slots = mutableListOf<TimeSlot>()
        val startHour = 9
        val endHour = 17

        for (hour in startHour until endHour) {
            for (min in listOf(0, 30)) {
                val timeString = formatTime(hour, min)
                val occupied = calculateOccupancy(hour, min)
                slots.add(TimeSlot(timeString, hour, min, occupied))
            }
        }
        _timeSlots.value = slots
    }

    private fun calculateOccupancy(hour: Int, min: Int): Int {
        var occupied = 0
        val targetStart = hour * 60 + min

        for (booking in existingBookings) {
            val bParts = booking.startTimeString.split(":", " ")
            if (bParts.size < 3) continue
            
            var bHour = bParts[0].toInt()
            val bMin = bParts[1].toInt()
            val bAmPm = bParts[2]
            
            if (bAmPm == "PM" && bHour != 12) bHour += 12
            if (bAmPm == "AM" && bHour == 12) bHour = 0
            
            val bStart = bHour * 60 + bMin
            val bEnd = bStart + booking.durationMinutes
            
            if (targetStart >= bStart && targetStart < bEnd) {
                occupied++
            }
        }
        return occupied
    }

    private fun formatTime(hour: Int, min: Int): String {
        val ampm = if (hour < 12) "AM" else "PM"
        val h = if (hour % 12 == 0) 12 else hour % 12
        return String.format(Locale.getDefault(), "%02d:%02d %s", h, min, ampm)
    }

    fun getEstimatedEndTime(startTimeString: String, duration: Int): String {
        val parts = startTimeString.split(":", " ")
        if (parts.size < 3) return "Unknown"
        
        var hour = parts[0].toInt()
        val min = parts[1].toInt()
        val amPm = parts[2]

        if (amPm == "PM" && hour != 12) hour += 12
        if (amPm == "AM" && hour == 12) hour = 0

        val totalMinutes = hour * 60 + min + duration
        val endHour = (totalMinutes / 60)
        val endMin = totalMinutes % 60
        
        return formatTime(endHour, endMin)
    }

    fun createAppointment(serviceId: String, doctorName: String, date: String, time: String) {
        viewModelScope.launch {
            _bookingState.value = Resource.Loading()
            _bookingState.value = repository.createAppointment(serviceId, doctorName, date, time)
        }
    }

    fun resetState() {
        _bookingState.value = null
    }
}
