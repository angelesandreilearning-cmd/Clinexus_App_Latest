package com.example.clinexusapp.ui.screens.appointments

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clinexusapp.data.model.DentalService
import com.example.clinexusapp.data.model.TimeSlot
import com.example.clinexusapp.ui.components.PremiumButton
import com.example.clinexusapp.ui.components.PremiumGlassCard
import com.example.clinexusapp.ui.components.PremiumTopAppBar
import com.example.clinexusapp.ui.theme.*
import com.example.clinexusapp.util.NotificationHelper
import com.example.clinexusapp.viewmodel.BookingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBookingScreen(
    doctorName: String = "Dr. Olivia Bennett",
    onBack: () -> Unit,
    onBookSuccess: () -> Unit,
    viewModel: BookingViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("Oct 24") }
    var selectedTime by remember { mutableStateOf<TimeSlot?>(null) }
    var isBookingConfirmed by remember { mutableStateOf(false) }
    
    val services by viewModel.services.collectAsState()
    val selectedService by viewModel.selectedService.collectAsState()
    val timeSlots by viewModel.timeSlots.collectAsState()

    Scaffold(
        topBar = {
            PremiumTopAppBar(
                title = "Book Appointment",
                onBack = if (isBookingConfirmed) null else onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                
                // Doctor Profile Section
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    DoctorProfileCard(doctorName)
                }
                
                // Step 1: Service
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SectionHeader("1. Select Treatment", Icons.Default.Info)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 8.dp)
                    ) {
                        items(services) { service ->
                            ServiceCard(
                                service = service,
                                isSelected = selectedService?.id == service.id,
                                onClick = { 
                                    if (!isBookingConfirmed) {
                                        viewModel.selectService(service)
                                        selectedTime = null
                                    }
                                }
                            )
                        }
                    }
                }

                // Step 2: Date
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SectionHeader("2. Select Date", Icons.Default.EventAvailable)
                    ModernDateSelector(selectedDate) { 
                        if (!isBookingConfirmed) selectedDate = it 
                    }
                }
                
                // Step 3: Slots
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SectionHeader("3. Preferred Time", Icons.Default.AccessTimeFilled)
                    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                        PremiumGlassCard {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text(
                                    text = "Availability Heatmap", 
                                    fontSize = 12.sp, 
                                    fontWeight = FontWeight.Bold,
                                    color = BluePrimary.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                SmartTimeGrid(
                                    timeSlots = timeSlots,
                                    selectedTime = selectedTime,
                                    onTimeSelected = { 
                                        if (!isBookingConfirmed) selectedTime = it 
                                    }
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    LegendItem("Available", BluePrimary.copy(alpha = 0.1f))
                                    LegendItem("Limited", BlueExtraLight)
                                    LegendItem("Reserved", GrayMedium.copy(alpha = 0.3f))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp)) // Padding for bottom summary
            }

            // Bottom Summary & Action
            AnimatedVisibility(
                visible = selectedTime != null && selectedService != null && !isBookingConfirmed,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                    shadowElevation = 24.dp,
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .navigationBarsPadding(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        if (selectedTime != null && selectedService != null) {
                            BookingSummaryContent(
                                startTime = selectedTime!!.time,
                                endTime = viewModel.getEstimatedEndTime(selectedTime!!.time, selectedService!!.durationMinutes),
                                serviceName = selectedService!!.name,
                                duration = selectedService!!.durationMinutes
                            )
                        }
                        
                        PremiumButton(
                            text = "Confirm Appointment",
                            onClick = {
                                NotificationHelper.showBookingNotification(context, doctorName)
                                isBookingConfirmed = true
                            }
                        )
                    }
                }
            }

            // Success Overlay
            AnimatedVisibility(
                visible = isBookingConfirmed,
                enter = fadeIn() + scaleIn(initialScale = 0.9f),
                exit = fadeOut()
            ) {
                BookingSuccessOverlay(
                    doctorName = doctorName,
                    date = selectedDate,
                    time = selectedTime?.time ?: "",
                    onDone = onBookSuccess
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon, 
            contentDescription = null, 
            tint = BluePrimary, 
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title, 
            fontWeight = FontWeight.Black, 
            fontSize = 18.sp, 
            color = BluePrimary,
            letterSpacing = (-0.5).sp
        )
    }
}

@Composable
fun DoctorProfileCard(name: String) {
    PremiumGlassCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .shadow(8.dp, RoundedCornerShape(20.dp))
                    .background(PremiumBlueGradient, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(2).uppercase(), 
                    color = White, 
                    fontWeight = FontWeight.Black, 
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = name, 
                    fontWeight = FontWeight.ExtraBold, 
                    fontSize = 22.sp, 
                    color = BluePrimary,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Lead Oral Specialist", 
                    color = MaterialTheme.colorScheme.onSurfaceVariant, 
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Surface(
                        color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                        shape = CircleShape
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(6.dp).background(Color(0xFF4CAF50), CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Available Today", color = Color(0xFF4CAF50), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceCard(service: DentalService, isSelected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(if (isSelected) 1.05f else 1f, label = "scale")
    
    Surface(
        onClick = onClick,
        modifier = Modifier
            .width(150.dp)
            .scale(scale),
        shape = RoundedCornerShape(24.dp),
        color = if (isSelected) BluePrimary else MaterialTheme.colorScheme.surface,
        shadowElevation = if (isSelected) 12.dp else 2.dp,
        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) else null
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (isSelected) White.copy(alpha = 0.2f) else BluePrimary.copy(alpha = 0.05f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = service.emoji, fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = service.name, 
                fontWeight = FontWeight.Bold, 
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = if (isSelected) White else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${service.durationMinutes} mins", 
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ModernDateSelector(selected: String, onSelect: (String) -> Unit) {
    val dates = listOf(
        "Mon" to "24",
        "Tue" to "25",
        "Wed" to "26",
        "Thu" to "27",
        "Fri" to "28"
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(dates) { (day, date) ->
            val fullDate = "Oct $date"
            val isSelected = selected == fullDate
            
            Surface(
                onClick = { onSelect(fullDate) },
                modifier = Modifier.width(64.dp),
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) BluePrimary else MaterialTheme.colorScheme.surface,
                shadowElevation = if (isSelected) 8.dp else 1.dp,
                border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) else null
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = day,
                        color = if (isSelected) White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = date,
                        color = if (isSelected) White else MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
fun SmartTimeGrid(
    timeSlots: List<TimeSlot>,
    selectedTime: TimeSlot?,
    onTimeSelected: (TimeSlot) -> Unit
) {
    val rows = timeSlots.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        rows.forEach { rowSlots ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowSlots.forEach { slot ->
                    Box(modifier = Modifier.weight(1f)) {
                        ModernTimeSlotItem(
                            slot = slot,
                            isSelected = selectedTime?.time == slot.time,
                            onClick = { onTimeSelected(slot) }
                        )
                    }
                }
                repeat(3 - rowSlots.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
fun ModernTimeSlotItem(slot: TimeSlot, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = when {
        slot.isFullyBooked -> GrayMedium.copy(alpha = 0.3f)
        isSelected -> Color.Transparent
        slot.hasLimitedAvailability -> BlueExtraLight
        else -> MaterialTheme.colorScheme.surface
    }
    
    val contentColor = when {
        slot.isFullyBooked -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        isSelected -> White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        onClick = onClick,
        enabled = !slot.isFullyBooked,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = if (isSelected) 8.dp else 0.dp,
        border = when {
            isSelected -> null
            slot.hasLimitedAvailability -> BorderStroke(1.dp, BluePrimary.copy(alpha = 0.2f))
            slot.isFullyBooked -> null
            else -> BorderStroke(1.5.dp, BluePrimary.copy(alpha = 0.05f))
        }
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (isSelected) Modifier.background(PremiumBlueGradient) else Modifier
                )
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = slot.time,
                    color = contentColor,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                )
                
                if (slot.isFullyBooked) {
                    Text(
                        text = "RESERVED",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black, 
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                } else if (slot.hasLimitedAvailability) {
                    Text(
                        text = "1 SPOT LEFT",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black, 
                        color = if (isSelected) White.copy(alpha = 0.8f) else BluePrimary
                    )
                }
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(4.dp))
                .border(0.5.dp, BluePrimary.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun BookingSuccessOverlay(
    doctorName: String,
    date: String,
    time: String,
    onDone: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BlueDark.copy(alpha = 0.95f) // Deep premium background for confirmation
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(
                        color = White.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(90.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Success!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Appointment Confirmed",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Success Card / Ticket
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "YOUR APPOINTMENT",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = BluePrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Dentist", fontSize = 13.sp, color = GrayDark)
                            Text(doctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Black)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Date", fontSize = 13.sp, color = GrayDark)
                            Text(date, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = GrayMedium)
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Selected Time", fontSize = 13.sp, color = GrayDark)
                    Text(time, fontWeight = FontWeight.Black, fontSize = 28.sp, color = BluePrimary)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Please arrive 10 minutes early.",
                        fontSize = 12.sp,
                        color = GrayDark,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(64.dp))
            
            Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Back to Dashboard", color = BluePrimary, fontWeight = FontWeight.Black, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun BookingSummaryContent(startTime: String, endTime: String, serviceName: String, duration: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(BluePrimary.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Schedule, contentDescription = null, tint = BluePrimary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$startTime - $endTime", 
                fontWeight = FontWeight.Black, 
                fontSize = 18.sp,
                color = BluePrimary
            )
            Text(
                text = "$serviceName • $duration mins", 
                fontSize = 14.sp, 
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
