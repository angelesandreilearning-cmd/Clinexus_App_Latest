package com.example.clinexusapp.ui.screens.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinexusapp.ui.components.*
import com.example.clinexusapp.ui.theme.*
import com.example.clinexusapp.ui.navigation.Screen
import com.example.clinexusapp.viewmodel.HistoryViewModel
import com.example.clinexusapp.util.Resource

import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentHistoryScreen(
    onBack: () -> Unit,
    onNavigateToBooking: () -> Unit,
    viewModel: HistoryViewModel
) {
    val historyState by viewModel.historyState.collectAsState()
    var selectedAppointment by remember { mutableStateOf<HistoryAppointment?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val appointments = listOf(
        HistoryAppointment("a1", "Dental Cleaning", "Oct 24, 2026 • 10:30 AM", Icons.Default.MedicalServices, VibrantTeal),
        HistoryAppointment("a2", "Orthodontic Checkup", "Aug 5, 2026 • 2:00 PM", Icons.Default.Settings, Color(0xFF00A896))
    )

    if (selectedAppointment != null) {
        val appt = selectedAppointment!!
        AlertDialog(
            onDismissRequest = { selectedAppointment = null },
            title = { Text(appt.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
            text = { Text("How would you like to manage this scheduled visit?") },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.rescheduleAppointment(appt.id, "Nov 1", "09:00 AM")
                    selectedAppointment = null
                    scope.launch { snackbarHostState.showSnackbar("RESCHEDULE: Pending Admin Approval") }
                }) {
                    Text("RESCHEDULE", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    viewModel.cancelAppointment(appt.id)
                    selectedAppointment = null
                    scope.launch { snackbarHostState.showSnackbar("CANCEL: Pending Admin Approval") }
                }) {
                    Text("CANCEL VISIT", color = Color.Red.copy(alpha = 0.7f))
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ElegantTopAppBar(
                title = "My Appointments",
                onBack = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = historyState) {
                is Resource.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is Resource.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message ?: "An error occurred", color = Color.Red)
                    }
                }
                is Resource.Success -> {
                    val appointmentsList = state.data ?: emptyList()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(top = 24.dp, bottom = 120.dp)
                    ) {
                        item {
                            Text(
                                text = "Scheduled Visits",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        
                        items(appointmentsList) { appointment ->
                            val isPending = appointment.status.contains("Pending", ignoreCase = true)
                            NeumorphicCard(modifier = Modifier.fillMaxWidth().clickable { 
                                selectedAppointment = HistoryAppointment(
                                    appointment.id,
                                    appointment.doctor,
                                    "${appointment.date} • ${appointment.time}",
                                    Icons.Default.MedicalServices,
                                    VibrantTeal
                                )
                            }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(VibrantTeal.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.MedicalServices, null, tint = VibrantTeal, modifier = Modifier.size(24.dp))
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = appointment.doctor, 
                                            fontWeight = FontWeight.Bold, 
                                            color = MaterialTheme.colorScheme.onSurface, 
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = "${appointment.date} • ${appointment.time}", 
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), 
                                            fontSize = 13.sp
                                        )
                                        if (isPending) {
                                            Surface(
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                                shape = RoundedCornerShape(4.dp),
                                                modifier = Modifier.padding(top = 4.dp)
                                            ) {
                                                Text(
                                                    text = appointment.status.uppercase(), 
                                                    fontSize = 9.sp, 
                                                    fontWeight = FontWeight.Black,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                    Icon(
                                        Icons.AutoMirrored.Filled.KeyboardArrowRight, 
                                        null, 
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                                }
                            }
                        }
                        
                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Need a New Appointment?",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        
                        item {
                            NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(160.dp)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.DateRange, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(80.dp))
                                    }
                                    Spacer(modifier = Modifier.height(24.dp))
                                    VibrantButton(
                                        text = "Book Appointment",
                                        onClick = onNavigateToBooking
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class HistoryAppointment(val id: String, val title: String, val date: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val color: Color)
