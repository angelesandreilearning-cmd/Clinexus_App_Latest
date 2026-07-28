package com.example.clinexusapp.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinexusapp.ui.components.PremiumGlassCard
import com.example.clinexusapp.ui.components.PremiumTopAppBar
import com.example.clinexusapp.ui.theme.BluePrimary
import com.example.clinexusapp.ui.theme.PremiumBlueGradient
import com.example.clinexusapp.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit, settingsViewModel: SettingsViewModel) {
    val darkMode by settingsViewModel.isDarkMode.collectAsState()
    var pushNotifications by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            PremiumTopAppBar(
                title = "Settings",
                onBack = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            item {
                SettingsSection(title = "App Settings") {
                    SettingsToggleItem(
                        title = "Dark Mode",
                        icon = Icons.Default.DarkMode,
                        isChecked = darkMode,
                        onCheckedChange = { settingsViewModel.toggleDarkMode(it) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    SettingsToggleItem(
                        title = "Push Notifications",
                        icon = Icons.Default.NotificationsActive,
                        isChecked = pushNotifications,
                        onCheckedChange = { pushNotifications = it }
                    )
                }
            }

            item {
                SettingsSection(title = "Privacy & Security") {
                    SettingsLinkItem(title = "Two-Step Verification", icon = Icons.Default.Security)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    SettingsLinkItem(title = "Privacy Policy", icon = Icons.Default.PrivacyTip)
                }
            }

            item {
                SettingsSection(title = "About") {
                    SettingsLinkItem(title = "App Version 1.0.0", icon = Icons.Default.Info)
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Clinexus Premium v1.0",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title.uppercase(), 
            fontSize = 12.sp, 
            fontWeight = FontWeight.Black, 
            color = BluePrimary,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )
        PremiumGlassCard {
            content()
        }
    }
}

@Composable
fun SettingsToggleItem(title: String, icon: ImageVector, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(BluePrimary.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title, 
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
        }
        Switch(
            checked = isChecked, 
            onCheckedChange = onCheckedChange, 
            colors = SwitchDefaults.colors(
                checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                checkedTrackColor = BluePrimary
            )
        )
    }
}

@Composable
fun SettingsLinkItem(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(BluePrimary.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title, 
            modifier = Modifier.weight(1f), 
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
    }
}
