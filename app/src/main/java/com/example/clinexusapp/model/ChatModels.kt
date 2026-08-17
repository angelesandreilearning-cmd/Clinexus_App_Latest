package com.example.clinexusapp.model

data class ContactDTO(
    val accountID: Int,
    val name: String,
    val role: String,
    val accountType: String
)

data class ConversationDTO(
    val conversation_id: Int,
    val account_id: Int,
    val account_type: String,
    val name: String,
    val role: String,
    val last_message: String?,
    val last_message_time: String?,
    val last_message_id: Int?
)

data class MessageDetailDTO(
    val message_id: Int,
    val account_id: Int,
    val account_type: String,
    val message_content: String,
    val message_time: String,
    val is_read: Boolean
)

data class ConversationMessagesResponse(
    val success: Boolean,
    val messages: List<MessageDetailDTO>,
    val otherParticipantLastReadMessageID: Int?
)

data class SendMessageRequest(
    val receiverAccountType: String,
    val receiverAccountID: Int,
    val messageContent: String
)

data class SendMessageResponse(
    val success: Boolean,
    val message: String,
    val message_id: Int? = null
)

data class MarkReadRequest(
    val lastReadMessageID: Int
)
