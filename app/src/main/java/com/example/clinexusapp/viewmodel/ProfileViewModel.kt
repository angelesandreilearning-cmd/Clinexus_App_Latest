package com.example.clinexusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinexusapp.api.AuthRepository
import com.example.clinexusapp.api.AddressRepository
import com.example.clinexusapp.model.*
import com.example.clinexusapp.util.Resource
import com.example.clinexusapp.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: AuthRepository,
    private val addressRepository: AddressRepository? = null
) : ViewModel() {

    private val _updateState = MutableStateFlow<Resource<GenericResponse>?>(null)
    val updateState = _updateState.asStateFlow()

    private val _regions = MutableStateFlow<List<Region>>(emptyList())
    val regions = _regions.asStateFlow()

    private val _provinces = MutableStateFlow<List<Province>>(emptyList())
    val provinces = _provinces.asStateFlow()

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities = _cities.asStateFlow()

    private val _barangays = MutableStateFlow<List<Barangay>>(emptyList())
    val barangays = _barangays.asStateFlow()

    init {
        loadRegions()
    }

    private fun loadRegions() {
        viewModelScope.launch {
            addressRepository?.getRegions()?.let { result ->
                if (result is Resource.Success) _regions.value = result.data ?: emptyList()
            }
        }
    }

    fun onRegionSelected(regionCode: String) {
        viewModelScope.launch {
            _provinces.value = emptyList()
            _cities.value = emptyList()
            _barangays.value = emptyList()
            addressRepository?.getProvinces(regionCode)?.let { result ->
                if (result is Resource.Success) _provinces.value = result.data ?: emptyList()
            }
        }
    }

    fun onProvinceSelected(provinceCode: String) {
        viewModelScope.launch {
            _cities.value = emptyList()
            _barangays.value = emptyList()
            addressRepository?.getCities(provinceCode)?.let { result ->
                if (result is Resource.Success) _cities.value = result.data ?: emptyList()
            }
        }
    }

    fun onCitySelected(cityCode: String) {
        viewModelScope.launch {
            _barangays.value = emptyList()
            addressRepository?.getBarangays(cityCode)?.let { result ->
                if (result is Resource.Success) _barangays.value = result.data ?: emptyList()
            }
        }
    }

    fun updateProfile(firstName: String, lastName: String, email: String) {
        viewModelScope.launch {
            _updateState.value = Resource.Loading()
            val user = SessionManager.currentUser.value
            val request = UpdateProfileRequest(
                firstName = firstName,
                middleName = user?.middleName,
                lastName = lastName,
                phoneNumber = user?.phoneNumber ?: "",
                dateOfBirth = user?.dateOfBirth ?: "",
                streetAddress = user?.streetAddress ?: "",
                province = user?.province ?: "",
                city = user?.city ?: "",
                barangay = user?.barangay ?: ""
            )
            val result = repository.updatePatientProfile(request)
            if (result is Resource.Success) refreshProfile()
            _updateState.value = result
        }
    }

    fun updateFullProfile(request: UpdateProfileRequest) {
        viewModelScope.launch {
            _updateState.value = Resource.Loading()
            val result = repository.updatePatientProfile(request)
            if (result is Resource.Success) refreshProfile()
            _updateState.value = result
        }
    }

    fun fetchProfile() {
        viewModelScope.launch {
            refreshProfile()
        }
    }

    private suspend fun refreshProfile() {
        repository.getPatientProfile().let { result ->
            if (result is Resource.Success) {
                SessionManager.updateProfile(result.data!!)
            }
        }
    }

    fun resetState() {
        _updateState.value = null
    }
}
