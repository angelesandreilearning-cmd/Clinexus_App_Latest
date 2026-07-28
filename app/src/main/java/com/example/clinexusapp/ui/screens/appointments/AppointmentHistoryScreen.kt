package com.example.clinexusapp.ui.screens.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinexusapp.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentHistoryScreen(onBack: () -> Unit) {
    val appointments = listOf(
        Appointment("Dr. Olivia Bennett", "Oct 24, 2026", "10:30 AM", "Completed"),
        Appointment("Dr. Liam Carter", "Oct 10, 2026", "02:00 PM", "Completed"),
        Appointment("Dr. Sarah Wilson", "Sep 28, 2026", "11:30 AM", "Cancelled"),
        Appointment("Dr. Olivia Bennett", "Aug 15, 2026", "09:00 AM", "Completed")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smile Journey", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Your dental history timeline",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                itemsIndexed(appointments) { index, appointment ->
                    TimelineItem(
                        appointment = appointment,
                        isLast = index == appointments.size - 1
                    )
                }
            }
        }
    }
}

data class Appointment(val doctor: String, val date: String, val time: String, val status: String)

@Composable
fun TimelineItem(appointment: Appointment, isLast: Boolean) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(48.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (appointment.status == "Completed") 
                            MaterialTheme.colorScheme.primary 
                        else 
                            Color.Red.copy(alpha = 0.8f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (appointment.status == "Completed") Icons.Default.Check else Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                )
            }
        }
        
        Column(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 32.dp)
                .fillMaxWidth()
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = appointment.date,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = appointment.status,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (appointment.status == "Completed") 
                                Color(0xFF4CAF50) 
                            else 
                                Color.Red.copy(alpha = 0.7f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = appointment.doctor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Consultation at ${appointment.time}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
