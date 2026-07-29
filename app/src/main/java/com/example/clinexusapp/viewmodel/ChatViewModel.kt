package com.example.clinexusapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinexusapp.api.AuthRepository
import com.example.clinexusapp.model.ChatMessageDTO
import com.example.clinexusapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _chatState = MutableStateFlow<Resource<List<ChatMessageDTO>>>(Resource.Loading())
    val chatState = _chatState.asStateFlow()

    init {
        fetchMessages()
    }

    fun fetchMessages() {
        viewModelScope.launch {
            _chatState.value = Resource.Loading()
            _chatState.value = repository.getChatMessages()
        }
    }

    fun sendMessage(text: String, isImage: Boolean = false) {
        // Simulated sending logic, usually would hit an API and then refresh
        fetchMessages()
    }
}
