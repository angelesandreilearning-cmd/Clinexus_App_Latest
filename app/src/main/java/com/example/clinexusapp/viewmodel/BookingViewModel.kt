package com.example.clinexusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinexusapp.api.AppointmentRepository
import com.example.clinexusapp.api.AuthRepository
import com.example.clinexusapp.model.*
import com.example.clinexusapp.util.Resource
import com.example.clinexusapp.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingViewModel(
    private val repository: AuthRepository,
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    private val _bookingState = MutableStateFlow<Resource<CreateAppointmentResponse>?>(null)
    val bookingState = _bookingState.asStateFlow()

    private val _dentistsState = MutableStateFlow<Resource<List<DentistDTO>>>(Resource.Loading())
    val dentistsState = _dentistsState.asStateFlow()

    private val _servicesState = MutableStateFlow<Resource<List<BookableServiceDTO>>>(Resource.Loading())
    val servicesState = _servicesState.asStateFlow()

    private val _selectedDentist = MutableStateFlow<DentistDTO?>(null)
    val selectedDentist = _selectedDentist.asStateFlow()

    private val _selectedServices = MutableStateFlow<List<BookableServiceDTO>>(emptyList())
    val selectedServices = _selectedServices.asStateFlow()

    private val _availableTimeslots = MutableStateFlow<Resource<List<AvailableSlotDTO>>>(Resource.Loading())
    val availableTimeslots = _availableTimeslots.asStateFlow()

    private val _selectedDate = MutableStateFlow("2026-08-20")
    val selectedDate = _selectedDate.asStateFlow()

    init {
        fetchDentists()
        fetchServices()
    }

    fun fetchDentists() {
        viewModelScope.launch {
            _dentistsState.value = Resource.Loading()
            val result = appointmentRepository.getActiveDentists()
            if (result is Resource.Success) {
                _dentistsState.value = Resource.Success(result.data?.take(4) ?: emptyList())
            } else {
                _dentistsState.value = result
            }
        }
    }

    fun fetchServices() {
        viewModelScope.launch {
            _servicesState.value = Resource.Loading()
            val result = appointmentRepository.getBookableServices()
            if (result is Resource.Success) {
                _servicesState.value = Resource.Success(result.data?.take(4) ?: emptyList())
            } else {
                _servicesState.value = result
            }
        }
    }

    fun selectDentist(dentist: DentistDTO) {
        _selectedDentist.value = dentist
        checkAndFetchTimeslots(_selectedDate.value)
    }

    fun toggleService(service: BookableServiceDTO) {
        val current = _selectedServices.value.toMutableList()
        if (current.any { it.serviceId == service.serviceId }) {
            current.removeAll { it.serviceId == service.serviceId }
        } else {
            current.add(service)
        }
        _selectedServices.value = current
    }

    fun checkAndFetchTimeslots(date: String) {
        _selectedDate.value = date
        val dentist = _selectedDentist.value

        if (dentist != null) {
            fetchTimeslots(dentist.dentistId, date)
        }
    }

    private var lastFetchedDentistId: Int? = null
    private var lastFetchedDate: String? = null

    private fun fetchTimeslots(dentistId: Int, date: String) {
        if (lastFetchedDentistId == dentistId && lastFetchedDate == date) return

        viewModelScope.launch {
            _availableTimeslots.value = Resource.Loading()
            val result = appointmentRepository.getAvailableTimeslots(dentistId, date)
            _availableTimeslots.value = result
            if (result is Resource.Success) {
                lastFetchedDentistId = dentistId
                lastFetchedDate = date
            }
        }
    }

    fun createAppointment(date: String, slot: AvailableSlotDTO) {
        val patientId = SessionManager.currentUser.value?.patientID
        val dentist = _selectedDentist.value
        val services = _selectedServices.value
        
        android.util.Log.d("BookingViewModel", "Attempting booking: patientId=$patientId, dentistId=${dentist?.dentistId}, servicesCount=${services.size}")

        if (patientId == null || patientId == 0) {
            _bookingState.value = Resource.Error("Error: Patient ID not found. Please log in again.")
            return
        }
        
        if (dentist == null) {
            _bookingState.value = Resource.Error("Error: No dentist selected.")
            return
        }
        
        if (services.isEmpty()) {
            _bookingState.value = Resource.Error("Error: Please select at least one service.")
            return
        }

        viewModelScope.launch {
            _bookingState.value = Resource.Loading()
            val request = CreateAppointmentRequest(
                patientId = patientId,
                dentistId = dentist.dentistId,
                appointmentDate = date,
                startTime = slot.startTime,
                endTime = slot.endTime,
                notes = "Mobile Booking",
                selectedServices = services.map { it.serviceId }
            )
            android.util.Log.d("BookingViewModel", "Sending request: $request")
            val result = appointmentRepository.createAppointment(request)
            _bookingState.value = result
            android.util.Log.d("BookingViewModel", "Booking result: ${result.javaClass.simpleName}")
        }
    }

    fun resetState() {
        _bookingState.value = null
    }
}
