package com.example.clinexusapp.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinexusapp.ui.components.ElegantTopAppBar
import com.example.clinexusapp.ui.components.NeumorphicCard
import com.example.clinexusapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(onBack: () -> Unit) {
    val notifications = listOf(
        NotificationItem("Appointment Confirmed", "Your appointment with Dr. Sarah Wilson has been confirmed for today at 10:30 AM.", "10 min ago", true),
        NotificationItem("New Message", "Dr. John Smith sent you a new message regarding your lab results.", "1 hour ago", true),
        NotificationItem("Medication Reminder", "It's time to take your morning dose of Vitamin D.", "3 hours ago", false),
        NotificationItem("Health Tip", "Staying hydrated is key to a healthy heart. Remember to drink 8 glasses of water today!", "Yesterday", false)
    )

    Scaffold(
        topBar = {
            ElegantTopAppBar(
                title = "Notifications",
                onBack = onBack
            )
        },
        containerColor = SoftMist
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            items(notifications) { item ->
                TealNotificationCard(item)
            }
        }
    }
}

data class NotificationItem(
    val title: String,
    val description: String,
    val time: String,
    val isUnread: Boolean
)

@Composable
fun TealNotificationCard(item: NotificationItem) {
    NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (item.isUnread) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else GrayMedium),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    tint = if (item.isUnread) MaterialTheme.colorScheme.primary else SlateGray,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalNavy
                    )
                    if (item.isUnread) {
                        Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
                    }
                }
                Text(
                    text = item.time,
                    fontSize = 12.sp,
                    color = SlateGray.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    color = SlateGray,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
