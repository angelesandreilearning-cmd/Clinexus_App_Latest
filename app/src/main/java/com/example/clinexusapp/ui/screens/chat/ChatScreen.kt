package com.example.clinexusapp.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinexusapp.ui.components.ElegantTopAppBar
import com.example.clinexusapp.ui.theme.*
import com.example.clinexusapp.viewmodel.ChatViewModel
import com.example.clinexusapp.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(onBack: () -> Unit, viewModel: ChatViewModel) {
    var messageText by remember { mutableStateOf("") }
    val chatState by viewModel.chatState.collectAsState()
    val messages = remember {
        mutableStateListOf(
            Message("Hello! How can I help you today?", false),
            Message("I have been feeling some tooth pain recently.", true),
            Message("I see. Can you describe the pain? Is it sharp or dull?", false)
        )
    }

    Scaffold(
        topBar = {
            ElegantTopAppBar(
                title = "Clinic Chat",
                onBack = onBack,
                actions = {
                    Surface(
                        modifier = Modifier.padding(end = 16.dp).size(10.dp),
                        shape = CircleShape,
                        color = VibrantTeal
                    ) {}
                }
            )
        },
        containerColor = SoftMist
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                when (chatState) {
                    is Resource.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    is Resource.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = chatState.message ?: "An error occurred", color = Color.Red)
                        }
                    }
                    is Resource.Success -> {
                        val messages = chatState.data ?: emptyList()
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(vertical = 24.dp)
                        ) {
                            items(messages) { message ->
                                TealChatBubble(Message(
                                    message.text,
                                    message.isFromUser,
                                    message.isImage
                                ))
                            }
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                shadowElevation = 12.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), RoundedCornerShape(24.dp)).padding(horizontal = 8.dp)
                ) {
                    IconButton(onClick = { /* Attach */ }) {
                        Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.primary)
                    }
                    
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Type your message...", fontSize = 14.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )
                    
                    IconButton(
                        onClick = {
                            if (messageText.isNotEmpty()) {
                                messages.add(Message(messageText, true))
                                messageText = ""
                            }
                        },
                        enabled = messageText.isNotEmpty()
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, null, tint = if (messageText.isNotEmpty()) MaterialTheme.colorScheme.primary else SlateGray.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

data class Message(val text: String, val isFromUser: Boolean, val isImage: Boolean = false)

@Composable
fun TealChatBubble(message: Message) {
    val isFromUser = message.isFromUser
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            color = if (isFromUser) MaterialTheme.colorScheme.primary else White,
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (isFromUser) 20.dp else 4.dp,
                bottomEnd = if (isFromUser) 4.dp else 20.dp
            ),
            shadowElevation = 2.dp
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                color = if (isFromUser) White else RoyalNavy,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
