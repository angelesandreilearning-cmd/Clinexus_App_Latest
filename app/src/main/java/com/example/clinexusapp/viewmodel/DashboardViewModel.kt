package com.example.clinexusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinexusapp.api.AuthRepository
import com.example.clinexusapp.model.ClinicNewsDTO
import com.example.clinexusapp.model.HealthInsightDTO
import com.example.clinexusapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _newsState = MutableStateFlow<Resource<List<ClinicNewsDTO>>>(Resource.Loading())
    val newsState = _newsState.asStateFlow()

    private val _insightsState = MutableStateFlow<Resource<List<HealthInsightDTO>>>(Resource.Loading())
    val insightsState = _insightsState.asStateFlow()

    init {
        fetchDashboardData()
    }

    fun fetchDashboardData() {
        viewModelScope.launch {
            _newsState.value = Resource.Loading()
            _insightsState.value = Resource.Loading()
            
            _newsState.value = repository.getClinicNews()
            _insightsState.value = repository.getHealthInsights()
        }
    }
}
