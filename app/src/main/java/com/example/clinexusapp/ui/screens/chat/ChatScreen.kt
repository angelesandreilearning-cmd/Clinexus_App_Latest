package com.example.clinexusapp.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinexusapp.model.ContactDTO
import com.example.clinexusapp.model.ConversationDTO
import com.example.clinexusapp.model.MessageDetailDTO
import com.example.clinexusapp.ui.components.ElegantTopAppBar
import com.example.clinexusapp.ui.components.VibrantButton
import com.example.clinexusapp.ui.theme.*
import com.example.clinexusapp.util.DateUtils
import com.example.clinexusapp.util.Resource
import com.example.clinexusapp.util.SessionManager
import com.example.clinexusapp.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(onBack: () -> Unit, viewModel: ChatViewModel) {
    var messageText by remember { mutableStateOf("") }
    val contactsState by viewModel.contactsState.collectAsState()
    val conversationsState by viewModel.conversationsState.collectAsState()
    val conversationMessagesState by viewModel.conversationMessagesState.collectAsState()
    val sendMessageState by viewModel.sendMessageState.collectAsState()
    
    val selectedConversation by viewModel.selectedConversation.collectAsState()
    val selectedContact by viewModel.selectedContact.collectAsState()
    
    val currentUser by SessionManager.currentUser.collectAsState()
    
    var currentView by remember { mutableStateOf(ChatView.CONVERSATIONS) }

    LaunchedEffect(sendMessageState) {
        if (sendMessageState is Resource.Success) {
            messageText = ""
            viewModel.resetSendMessageState()
        }
    }

    Scaffold(
        topBar = {
            ElegantTopAppBar(
                title = when(currentView) {
                    ChatView.CONVERSATIONS -> "Messages"
                    ChatView.CONTACTS -> "New Chat"
                    ChatView.MESSAGES -> selectedConversation?.name ?: selectedContact?.name ?: "Clinic Chat"
                },
                onBack = {
                    when(currentView) {
                        ChatView.CONVERSATIONS -> onBack()
                        ChatView.CONTACTS -> {
                            viewModel.selectContact(null)
                            currentView = ChatView.CONVERSATIONS
                        }
                        ChatView.MESSAGES -> {
                            viewModel.selectConversation(null)
                            currentView = ChatView.CONVERSATIONS
                        }
                    }
                },
                actions = {
                    if (currentView == ChatView.MESSAGES) {
                        Surface(
                            modifier = Modifier.padding(end = 16.dp).size(10.dp),
                            shape = CircleShape,
                            color = VibrantTeal
                        ) {}
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentView == ChatView.CONVERSATIONS) {
                FloatingActionButton(
                    onClick = { currentView = ChatView.CONTACTS },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "New Chat",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        containerColor = SoftMist
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (currentView) {
                ChatView.CONVERSATIONS -> {
                    Box(modifier = Modifier.weight(1f)) {
                        when (conversationsState) {
                            is Resource.Loading -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                }
                            }
                            is Resource.Error -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = conversationsState.message ?: "An error occurred", color = Color.Red)
                                }
                            }
                            is Resource.Success -> {
                                val conversations = conversationsState.data ?: emptyList()
                                if (conversations.isEmpty()) {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = "No active conversations", color = SlateGray)
                                            Spacer(modifier = Modifier.height(16.dp))
                                            VibrantButton(
                                                text = "Start a New Chat",
                                                onClick = { currentView = ChatView.CONTACTS },
                                                modifier = Modifier.width(200.dp)
                                            )
                                        }
                                    }
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        contentPadding = PaddingValues(vertical = 16.dp)
                                    ) {
                                        items(conversations) { conversation ->
                                            ConversationItem(conversation = conversation) {
                                                viewModel.selectConversation(conversation)
                                                currentView = ChatView.MESSAGES
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                ChatView.CONTACTS -> {
                    Box(modifier = Modifier.weight(1f)) {
                        when (contactsState) {
                            is Resource.Loading -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                }
                            }
                            is Resource.Error -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = contactsState.message ?: "An error occurred", color = Color.Red)
                                }
                            }
                            is Resource.Success -> {
                                val contacts = contactsState.data ?: emptyList()
                                if (contacts.isEmpty()) {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(text = "No contacts available", color = SlateGray)
                                    }
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        contentPadding = PaddingValues(vertical = 16.dp)
                                    ) {
                                        items(contacts) { contact ->
                                            ContactItem(contact = contact) {
                                                viewModel.selectContact(contact)
                                                currentView = ChatView.MESSAGES
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                ChatView.MESSAGES -> {
                    // Automatic Refresh Polling is now handled in ViewModel via selectConversation
                    // But we still need to dispose if we leave ChatView.MESSAGES
                    DisposableEffect(Unit) {
                        onDispose {
                            viewModel.stopPollingMessages()
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        val isNewChat = selectedConversation == null
                        if (isNewChat) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = "Start chatting with ${selectedContact?.name}", color = SlateGray)
                            }
                        } else {
                            when (conversationMessagesState) {
                                is Resource.Loading -> {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                is Resource.Error -> {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(text = conversationMessagesState.message ?: "An error occurred", color = Color.Red)
                                    }
                                }
                                is Resource.Success -> {
                                    val messagesData = conversationMessagesState.data
                                    val messages = messagesData?.messages ?: emptyList()
                                    val lastReadId = messagesData?.otherParticipantLastReadMessageID ?: -1
                                    
                                    // Mark as read if there are messages
                                    LaunchedEffect(messages) {
                                        if (messages.isNotEmpty()) {
                                            selectedConversation?.let { conv ->
                                                viewModel.markConversationAsRead(conv.conversation_id, messages.last().message_id)
                                            }
                                        }
                                    }

                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        contentPadding = PaddingValues(vertical = 24.dp)
                                    ) {
                                        items(messages) { message ->
                                            val isFromMe = message.account_type == "patient" && message.account_id == (currentUser?.accountID ?: -1)
                                            val isSeen = isFromMe && message.message_id <= lastReadId
                                            TealChatBubble(message = message, isFromMe = isFromMe, isSeen = isSeen)
                                        }
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
                                        val receiverType = selectedConversation?.account_type ?: selectedContact?.accountType ?: ""
                                        val receiverId = selectedConversation?.account_id ?: selectedContact?.accountID ?: -1
                                        
                                        viewModel.sendMessage(
                                            receiverAccountType = receiverType,
                                            receiverAccountID = receiverId,
                                            messageContent = messageText,
                                            conversationID = selectedConversation?.conversation_id
                                        )
                                    }
                                },
                                enabled = messageText.isNotEmpty() && sendMessageState !is Resource.Loading
                            ) {
                                if (sendMessageState is Resource.Loading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                                } else {
                                    Icon(Icons.AutoMirrored.Filled.Send, null, tint = if (messageText.isNotEmpty()) MaterialTheme.colorScheme.primary else SlateGray.copy(alpha = 0.3f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class ChatView { CONVERSATIONS, CONTACTS, MESSAGES }

@Composable
fun ConversationItem(conversation: ConversationDTO, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = conversation.name.take(1).uppercase(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = conversation.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = conversation.role,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = conversation.last_message ?: "Start a conversation",
                    fontSize = 14.sp,
                    color = SlateGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            if (conversation.last_message_time != null) {
                Text(
                    text = DateUtils.formatChatTime(conversation.last_message_time),
                    fontSize = 11.sp,
                    color = SlateGray.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun ContactItem(contact: ContactDTO, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.name.take(1).uppercase(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = contact.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = contact.role,
                    fontSize = 13.sp,
                    color = SlateGray
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                null,
                tint = SlateGray.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun TealChatBubble(message: MessageDetailDTO, isFromMe: Boolean, isSeen: Boolean = false) {
    // Inverted Alignment: Patient (isFromMe) on LEFT, Staff on RIGHT
    val alignment = if (isFromMe) Alignment.CenterStart else Alignment.CenterEnd
    val horizontalAlignment = if (isFromMe) Alignment.Start else Alignment.End
    val bubbleColor = if (isFromMe) MaterialTheme.colorScheme.primary else White
    val textColor = if (isFromMe) White else RoyalNavy
    val shape = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp,
        bottomStart = if (isFromMe) 4.dp else 20.dp,
        bottomEnd = if (isFromMe) 20.dp else 4.dp
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(horizontalAlignment = horizontalAlignment) {
            Surface(
                color = bubbleColor,
                shape = shape,
                shadowElevation = 2.dp
            ) {
                Text(
                    text = message.message_content,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = textColor,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = DateUtils.formatChatTime(message.message_time),
                    fontSize = 11.sp,
                    color = SlateGray.copy(alpha = 0.6f),
                )
                if (isFromMe && isSeen) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "• Seen",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
