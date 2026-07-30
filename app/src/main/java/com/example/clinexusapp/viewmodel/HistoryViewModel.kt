package com.example.clinexusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinexusapp.api.AuthRepository
import com.example.clinexusapp.model.AppointmentDTO
import com.example.clinexusapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _historyState = MutableStateFlow<Resource<List<AppointmentDTO>>>(Resource.Loading())
    val historyState = _historyState.asStateFlow()

    init {
        fetchHistory()
    }

    fun fetchHistory() {
        viewModelScope.launch {
            _historyState.value = Resource.Loading()
            _historyState.value = repository.getAppointmentHistory()
        }
    }

    fun rescheduleAppointment(id: String, newDate: String, newTime: String) {
        viewModelScope.launch {
            repository.updateAppointment(id, mapOf("date" to newDate, "time" to newTime))
            fetchHistory()
        }
    }

    fun cancelAppointment(id: String) {
        viewModelScope.launch {
            repository.cancelAppointment(id)
            fetchHistory()
        }
    }
}
