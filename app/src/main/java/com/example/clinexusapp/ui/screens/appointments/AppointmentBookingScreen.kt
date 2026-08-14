package com.example.clinexusapp.ui.screens.appointments

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clinexusapp.data.model.DentalService
import com.example.clinexusapp.data.model.TimeSlot
import com.example.clinexusapp.ui.components.*
import com.example.clinexusapp.ui.theme.*
import com.example.clinexusapp.util.NotificationHelper
import com.example.clinexusapp.util.Resource
import com.example.clinexusapp.viewmodel.BookingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBookingScreen(
    doctorName: String,
    onBack: () -> Unit,
    onBookSuccess: () -> Unit,
    viewModel: BookingViewModel = viewModel()
) {
    val context = LocalContext.current
    
    // Generate dynamic dates (Next 7 days)
    val dynamicDates = remember {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("MMM d", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        List(7) {
            val date = calendar.time
            val label = "${dayFormat.format(date)} ${format.format(date).split(" ").last()}"
            val fullValue = format.format(date)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            label to fullValue
        }
    }

    var selectedDate by remember { mutableStateOf(dynamicDates.first().second) }
    var selectedDateLabel by remember { mutableStateOf(dynamicDates.first().first) }
    var selectedTime by remember { mutableStateOf<TimeSlot?>(null) }
    var isBookingConfirmed by remember { mutableStateOf(false) }
    
    val services by viewModel.services.collectAsState()
    val selectedService by viewModel.selectedService.collectAsState()
    val timeSlots by viewModel.timeSlots.collectAsState()
    val bookingState by viewModel.bookingState.collectAsState()

    LaunchedEffect(bookingState) {
        if (bookingState is Resource.Success) {
            NotificationHelper.showBookingNotification(context, doctorName)
            isBookingConfirmed = true
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            ElegantTopAppBar(
                title = "Schedule Visit",
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
                
                // Doctor Summary
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    ElegantDoctorSummary(doctorName)
                }
                
                // Step 1: Services
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    BookingSectionHeader("1. Select Service", Icons.Default.Info)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 8.dp)
                    ) {
                        items(services) { service ->
                            BookingServiceItem(
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
                    BookingSectionHeader("2. Select Date", Icons.Default.Event)
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        items(dynamicDates) { datePair ->
                            val isSelected = selectedDateLabel == datePair.first
                            Surface(
                                onClick = { 
                                    if (!isBookingConfirmed) {
                                        selectedDateLabel = datePair.first
                                        selectedDate = datePair.second
                                    }
                                },
                                modifier = Modifier.width(80.dp),
                                shape = RoundedCornerShape(18.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
                                shadowElevation = if (isSelected) 4.dp else 1.dp,
                                border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)) else null
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = datePair.first.split(" ").first().uppercase(), 
                                        color = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), 
                                        fontSize = 11.sp, 
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = datePair.first.split(" ").last(), 
                                        color = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface, 
                                        fontSize = 20.sp, 
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Step 3: Time
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    BookingSectionHeader("3. Preferred Time", Icons.Default.AccessTime)
                    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                        NeumorphicCard {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                BookingTimeGrid(
                                    timeSlots = timeSlots,
                                    selectedTime = selectedTime,
                                    onTimeSelected = { 
                                        if (!isBookingConfirmed) selectedTime = it 
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(140.dp))
            }

            // Bottom Bar
            AnimatedVisibility(
                visible = selectedTime != null && selectedService != null && !isBookingConfirmed,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 16.dp,
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .navigationBarsPadding(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        if (selectedTime != null && selectedService != null) {
                            BookingSummaryRow(
                                time = selectedTime!!.time,
                                service = selectedService!!.name,
                                duration = selectedService!!.durationMinutes
                            )
                        }
                        
                        VibrantButton(
                            text = if (bookingState is Resource.Loading) "Processing..." else "Authorize Booking",
                            onClick = {
                                viewModel.createAppointment(
                                    serviceId = selectedService?.id ?: "",
                                    doctorName = doctorName,
                                    date = selectedDate,
                                    time = selectedTime?.time ?: ""
                                )
                            },
                            enabled = bookingState !is Resource.Loading
                        )
                    }
                }
            }

            // Success Overlay
            if (isBookingConfirmed) {
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
fun BookingSectionHeader(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title, 
            fontWeight = FontWeight.Bold, 
            fontSize = 18.sp, 
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun BookingServiceItem(service: DentalService, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(22.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        shadowElevation = if (isSelected) 6.dp else 1.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = service.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = service.name, 
                fontWeight = FontWeight.Bold, 
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${service.durationMinutes} min", 
                fontSize = 12.sp,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun BookingDateSelector(selected: String, onSelect: (String) -> Unit) {
    // This is no longer used, kept for reference or can be deleted
}

@Composable
fun BookingTimeGrid(timeSlots: List<TimeSlot>, selectedTime: TimeSlot?, onTimeSelected: (TimeSlot) -> Unit) {
    val rows = timeSlots.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        rows.forEach { rowSlots ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowSlots.forEach { slot ->
                    Box(modifier = Modifier.weight(1f)) {
                        BookingTimeSlotItem(slot = slot, isSelected = selectedTime?.time == slot.time, onClick = { onTimeSelected(slot) })
                    }
                }
                repeat(3 - rowSlots.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
fun BookingTimeSlotItem(slot: TimeSlot, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = when {
        slot.isFullyBooked -> MaterialTheme.colorScheme.outline.copy(alpha = 0.05f)
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
    }
    Surface(
        onClick = onClick,
        enabled = !slot.isFullyBooked,
        shape = RoundedCornerShape(14.dp),
        color = bgColor,
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        border = if (!isSelected && !slot.isFullyBooked) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)) else null
    ) {
        Box(modifier = Modifier.padding(vertical = 14.dp), contentAlignment = Alignment.Center) {
            Text(
                text = slot.time, 
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else if (slot.isFullyBooked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f) else MaterialTheme.colorScheme.onSurface, 
                fontSize = 14.sp, 
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BookingSummaryRow(time: String, service: String, duration: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Schedule, null, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = time, 
                fontWeight = FontWeight.Bold, 
                fontSize = 18.sp, 
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$service • $duration min", 
                fontSize = 14.sp, 
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun BookingSuccessOverlay(doctorName: String, date: String, time: String, onDone: () -> Unit) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
        launch {
            alpha.animateTo(1f, tween(800))
        }
        delay(3000)
        onDone()
    }

    Surface(
        modifier = Modifier.fillMaxSize(), 
        color = primaryColor.copy(alpha = 0.98f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .scale(scale.value)
                .alpha(alpha.value), 
            horizontalAlignment = Alignment.CenterHorizontally, 
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(onPrimaryColor.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.CheckCircle, null, tint = onPrimaryColor, modifier = Modifier.size(70.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Session Authorized", color = onPrimaryColor, fontWeight = FontWeight.Black, fontSize = 28.sp)
            Text("REDIRECTING TO HOME...", color = onPrimaryColor.copy(alpha = 0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            
            Spacer(modifier = Modifier.height(48.dp))
            NeumorphicCard {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("APPOINTMENT PASS", color = primaryColor, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(doctorName, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(date, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(time, color = primaryColor, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("REF ID: #CX-${UUID.randomUUID().toString().take(6).uppercase()}", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), fontSize = 10.sp)
                }
            }
        }
    }
}
