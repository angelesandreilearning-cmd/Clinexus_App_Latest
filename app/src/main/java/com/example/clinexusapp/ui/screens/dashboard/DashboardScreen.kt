package com.example.clinexusapp.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clinexusapp.ui.components.*
import com.example.clinexusapp.ui.navigation.Screen
import com.example.clinexusapp.ui.theme.BluePrimary
import com.example.clinexusapp.ui.theme.PeachPrimary
import com.example.clinexusapp.ui.theme.PremiumBlueGradient
import com.example.clinexusapp.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(navController: NavController, rootNavController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var visible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        delay(500)
        isLoading = false
        visible = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("Close") }
            },
            title = { Text("Feature Coming Soon") },
            text = { Text("This premium feature is currently being refined.") }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        if (isLoading) {
            DashboardSkeleton()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { -20 }
                    ) {
                        Column {
                            PremiumHeader(
                                title = "Hello, Angel!",
                                subtitle = "Find your dental specialist",
                                onNotificationClick = { rootNavController.navigate(Screen.Notifications.route) }
                            )
                            Spacer(modifier = Modifier.height(28.dp))
                            Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                                SearchSection(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                }

                item {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(800)) + slideInVertically(tween(800)) { 20 }
                    ) {
                        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                            UpcomingAppointmentSection(onJoinCall = {
                                rootNavController.navigate(Screen.Chat.route)
                            })
                        }
                    }
                    Spacer(modifier = Modifier.height(36.dp))
                }

                item {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(1000))
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                            QuickActionsSection(navController, rootNavController, onComingSoon = { showDialog = true })
                            Spacer(modifier = Modifier.height(36.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Dental Specialists",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                TextButton(onClick = { navController.navigate(Screen.DoctorList.route) }) {
                                    Text("See All", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                }
                            }
                            Spacer(modifier = Modifier.height(18.dp))
                        }
                    }
                }

                item {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(1200))
                    ) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(18.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp)
                        ) {
                            items(listOf("Dr. Olivia Bennett", "Dr. Liam Carter")) { doctor ->
                                DoctorCard(name = doctor, onClick = {
                                    rootNavController.navigate(Screen.AppointmentBooking.route)
                                })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(36.dp))
                }

                item {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(1400))
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                            Text(
                                text = "Premium Health Insights",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(18.dp),
                                contentPadding = PaddingValues(horizontal = 24.dp)
                            ) {
                                items(listOf(
                                    Article("10 Dental Tips", "Wellness"),
                                    Article("Oral Hygiene", "Guide"),
                                    Article("Healthy Smile", "Lifestyle")
                                )) { article ->
                                    ArticleCard(article) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Opening: ${article.title}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

data class Article(val title: String, val category: String)

@Composable
fun ArticleCard(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .premiumClickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AutoAwesome, 
                    contentDescription = null, 
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), 
                    modifier = Modifier.size(44.dp)
                )
            }
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = article.category.uppercase(), 
                    color = MaterialTheme.colorScheme.primary, 
                    fontSize = 11.sp, 
                    fontWeight = FontWeight.ExtraBold, 
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = article.title, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 18.sp, 
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun SearchSection(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Search services...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        leadingIcon = { 
            Icon(
                Icons.Default.Search, 
                contentDescription = null, 
                tint = MaterialTheme.colorScheme.primary
            ) 
        },
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        )
    )
}

@Composable
fun UpcomingAppointmentSection(onJoinCall: () -> Unit) {
    PremiumCard(
        modifier = Modifier.fillMaxWidth(),
        gradient = PremiumBlueGradient
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "DR", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(18.dp))
            Column {
                Text(text = "Dr. Olivia Bennett", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "Senior Dentist", color = Color.White.copy(alpha = 0.85f), fontSize = 15.sp)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.25f))
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Today, 10:30 AM", color = Color.White, fontSize = 15.sp)
            }
            Button(
                onClick = onJoinCall,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(text = "Join Chat", color = BluePrimary, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun QuickActionsSection(
    navController: NavController, 
    rootNavController: NavController,
    onComingSoon: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        QuickActionItem("Consult", Icons.Default.MedicalServices) {
            navController.navigate(Screen.DoctorList.route)
        }
        QuickActionItem("Pharmacy", Icons.Default.LocalPharmacy) {
            onComingSoon()
        }
        QuickActionItem("Records", Icons.AutoMirrored.Filled.Assignment) {
            onComingSoon()
        }
        QuickActionItem("History", Icons.Default.History) {
            rootNavController.navigate(Screen.AppointmentHistory.route)
        }
    }
}

@Composable
fun QuickActionItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.premiumClickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .shadow(
                    elevation = 8.dp, 
                    shape = RoundedCornerShape(22.dp), 
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), 
                    shape = RoundedCornerShape(22.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon, 
                contentDescription = title, 
                tint = MaterialTheme.colorScheme.primary, 
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title, 
            fontSize = 14.sp, 
            fontWeight = FontWeight.Bold, 
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun DoctorCard(name: String, onClick: () -> Unit) {
    PremiumGlassCard(
        modifier = Modifier
            .width(180.dp)
            .premiumClickable { onClick() }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .shadow(4.dp, CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.split(" ").last().take(1),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = name,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Dentist", 
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), 
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
