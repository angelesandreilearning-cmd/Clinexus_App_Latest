package com.example.clinexusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clinexusapp.api.AddressRepository
import com.example.clinexusapp.api.AppointmentRepository
import com.example.clinexusapp.api.AuthRepository

class ViewModelFactory(
    private val repository: AuthRepository,
    private val addressRepository: AddressRepository? = null,
    private val appointmentRepository: AppointmentRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository, addressRepository!!) as T
        }
        if (modelClass.isAssignableFrom(OTPViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OTPViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(repository, appointmentRepository!!) as T
        }
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository, appointmentRepository!!) as T
        }
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository, appointmentRepository!!) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository, addressRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
