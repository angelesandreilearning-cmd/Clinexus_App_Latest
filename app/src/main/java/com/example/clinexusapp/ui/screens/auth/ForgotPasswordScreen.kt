package com.example.clinexusapp.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinexusapp.ui.components.ElegantTextField
import com.example.clinexusapp.ui.components.ElegantButton
import com.example.clinexusapp.ui.components.ElegantCard

@Composable
fun ForgotPasswordScreen(
    onSendResetLink: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Recovery",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = (-1).sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter your email to receive a\nsecure link for password reset.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(48.dp))

            ElegantCard {
                ElegantTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    icon = Icons.Default.Email
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            ElegantButton(
                text = "Send Link",
                onClick = onSendResetLink
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onNavigateBack) {
                Text(
                    text = "Back to login", 
                    color = MaterialTheme.colorScheme.primary, 
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
