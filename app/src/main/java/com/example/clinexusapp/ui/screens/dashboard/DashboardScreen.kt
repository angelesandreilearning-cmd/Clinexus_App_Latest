package com.example.clinexusapp.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clinexusapp.ui.components.*
import com.example.clinexusapp.ui.navigation.Screen
import com.example.clinexusapp.ui.theme.*
import com.example.clinexusapp.util.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.Launch

data class DashboardArticle(val title: String, val category: String)

@Composable
fun DashboardScreen(navController: NavController, rootNavController: NavController) {
    var showInsightDialog by remember { mutableStateOf<DashboardArticle?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    val user by SessionManager.currentUser.collectAsState()
    val firstName = user?.firstName ?: "Patient"

    LaunchedEffect(Unit) {
        delay(300)
        isLoading = false
    }

    if (showInsightDialog != null) {
        val article = showInsightDialog!!
        AlertDialog(
            onDismissRequest = { showInsightDialog = null },
            confirmButton = {
                TextButton(onClick = { showInsightDialog = null }) { 
                    Text("DONE", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) 
                }
            },
            title = { 
                Text(
                    text = article.title.uppercase(), 
                    color = MaterialTheme.colorScheme.primary, 
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ) 
            },
            text = { 
                Text(
                    text = "Professional clinical data regarding ${article.category.lowercase()} is being synchronized. Stay tuned for expert health tips!",
                    color = MaterialTheme.colorScheme.onSurface
                ) 
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (isLoading) {
            DashboardSkeleton()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = padding.calculateBottomPadding())
            ) {
                item {
                    WavyTealHeader(
                        title = "Hello, $firstName!",
                        subtitle = "Welcome back!"
                        // No back button or menu icon on Dashboard
                    )
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 24.dp).padding(top = 16.dp)) {
                        Text(
                            text = "Upcoming Appointment",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(14.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.EventNote, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(26.dp))
                                    }
                                    Spacer(modifier = Modifier.width(18.dp))
                                    Column {
                                        Text(
                                            text = "Dental Cleaning", 
                                            fontWeight = FontWeight.Bold, 
                                            color = MaterialTheme.colorScheme.onSurface, 
                                            fontSize = 17.sp
                                        )
                                        Text(
                                            text = "July 30, 2026 • 10:00 AM", 
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), 
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier.size(64.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        progress = { 0.75f },
                                        modifier = Modifier.fillMaxSize(),
                                        color = MaterialTheme.colorScheme.primary,
                                        strokeWidth = 4.dp,
                                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    )
                                    Icon(Icons.Default.Schedule, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Surface(
                                onClick = { rootNavController.navigate(Screen.AppointmentHistory.route) },
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(
                                    text = "View Details", 
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                                    fontSize = 13.sp, 
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 24.dp).offset(y = (-5).dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Health Insights", 
                                fontSize = 19.sp, 
                                fontWeight = FontWeight.Bold, 
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(3) { index ->
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 3.dp)
                                            .size(if (index == 0) 8.dp else 6.dp)
                                            .background(
                                                if (index == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), 
                                                CircleShape
                                            )
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                            InsightItem(
                                title = "Tips for a Brighter Smile",
                                subtitle = "Essential dental care tips you should know",
                                icon = Icons.Default.AutoFixHigh,
                                color = Color(0xFFFF8A65),
                                bgColor = Color(0xFFFFEAE3),
                                onClick = { showInsightDialog = DashboardArticle("Tips for a Brighter Smile", "Care") }
                            )
                            InsightItem(
                                title = "Stay Hydrated!",
                                subtitle = "The benefits of drinking more water",
                                icon = Icons.Default.WaterDrop,
                                color = Color(0xFF0288D1),
                                bgColor = Color(0xFFE1F5FE),
                                onClick = { showInsightDialog = DashboardArticle("Stay Hydrated!", "Health") }
                            )
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(), 
                            horizontalArrangement = Arrangement.SpaceBetween, 
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Clinic News", 
                                    fontSize = 19.sp, 
                                    fontWeight = FontWeight.Bold, 
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Box(modifier = Modifier.padding(start = 8.dp).size(6.dp).background(MaterialTheme.colorScheme.secondary, CircleShape))
                            }
                            IconButton(onClick = { /* simulated web link */ }) { 
                                Icon(Icons.AutoMirrored.Filled.Launch, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp)) 
                            }
                        }
                        NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                            Column {
                                Text(
                                    text = "Our Clinic is Expanding!",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Exciting new facilities coming soon to serve you better with the latest dental technology.",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InsightItem(title: String, subtitle: String, icon: ImageVector, color: Color, bgColor: Color, onClick: () -> Unit) {
    NeumorphicCard(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(bgColor, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(18.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title, 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.onSurface, 
                    fontSize = 16.sp
                )
                Text(
                    text = subtitle, 
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), 
                    fontSize = 13.sp, 
                    maxLines = 1
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight, 
                null, 
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), 
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
