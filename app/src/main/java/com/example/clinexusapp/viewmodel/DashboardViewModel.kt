package com.example.clinexusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinexusapp.api.AuthRepository
import com.example.clinexusapp.model.AppointmentDTO
import com.example.clinexusapp.model.ClinicNewsDTO
import com.example.clinexusapp.model.HealthInsightDTO
import com.example.clinexusapp.util.Resource
import com.example.clinexusapp.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _newsState = MutableStateFlow<Resource<List<ClinicNewsDTO>>>(Resource.Loading())
    val newsState = _newsState.asStateFlow()

    private val _insightsState = MutableStateFlow<Resource<List<HealthInsightDTO>>>(Resource.Loading())
    val insightsState = _insightsState.asStateFlow()

    private val _nextAppointment = MutableStateFlow<AppointmentDTO?>(null)
    val nextAppointment = _nextAppointment.asStateFlow()

    init {
        fetchDashboardData()
    }

    fun fetchDashboardData() {
        viewModelScope.launch {
            _newsState.value = Resource.Loading()
            _insightsState.value = Resource.Loading()
            
            // Parallel execution
            launch {
                val profileResult = repository.getPatientProfile()
                if (profileResult is Resource.Success && profileResult.data != null) {
                    SessionManager.updateProfile(profileResult.data)
                }
            }

            launch {
                val historyResult = repository.getAppointmentHistory()
                if (historyResult is Resource.Success && historyResult.data != null) {
                    // Assuming history is sorted by date or we need to find the closest future one
                    _nextAppointment.value = historyResult.data.firstOrNull() 
                }
            }

            launch {
                _newsState.value = repository.getClinicNews()
            }

            launch {
                _insightsState.value = repository.getHealthInsights()
            }
        }
    }
}
