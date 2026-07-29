package com.example.clinexusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinexusapp.api.AuthRepository
import com.example.clinexusapp.model.RegisterRequest
import com.example.clinexusapp.model.RegisterResponse
import com.example.clinexusapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _registerState = MutableStateFlow<Resource<RegisterResponse>?>(null)
    val registerState = _registerState.asStateFlow()

    private val _validationError = MutableStateFlow<String?>(null)
    val validationError = _validationError.asStateFlow()

    fun register(
        email: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        middleName: String,
        lastName: String,
        phoneNumber: String,
        dateOfBirth: String,
        sex: String
    ) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || 
            password.isEmpty() || phoneNumber.isEmpty() || dateOfBirth.isEmpty()
        ) {
            _validationError.value = "Please fill in all required fields"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _validationError.value = "Please enter a valid email address"
            return
        }

        if (password.length < 6) {
            _validationError.value = "Password must be at least 6 characters"
            return
        }

        if (password != confirmPassword) {
            _validationError.value = "Passwords do not match"
            return
        }

        _validationError.value = null
        viewModelScope.launch {
            _registerState.value = Resource.Loading()
            val request = RegisterRequest(
                email, password, firstName, middleName, lastName, phoneNumber, dateOfBirth, sex
            )
            _registerState.value = repository.register(request)
        }
    }

    fun resetState() {
        _registerState.value = null
        _validationError.value = null
    }
}
