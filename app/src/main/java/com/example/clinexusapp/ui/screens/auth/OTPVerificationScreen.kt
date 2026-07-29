package com.example.clinexusapp.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinexusapp.ui.components.ElegantTextField
import com.example.clinexusapp.ui.components.ElegantButton
import com.example.clinexusapp.ui.components.ElegantCard

@Composable
fun OTPVerificationScreen(
    phoneNumber: String = "+1 234 567 8900",
    onVerifySuccess: () -> Unit,
    onResendOTP: () -> Unit
) {
    var otpCode by remember { mutableStateOf("") }

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
                text = "Verification",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = (-1).sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter the secure code sent to\n$phoneNumber",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(48.dp))

            ElegantCard {
                ElegantTextField(
                    value = otpCode,
                    onValueChange = { if (it.length <= 6) otpCode = it },
                    label = "Activation Code",
                    icon = Icons.Default.Pin
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            ElegantButton(
                text = "Verify Code",
                onClick = onVerifySuccess
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Code not received? ", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                TextButton(onClick = onResendOTP) {
                    Text(
                        text = "Resend", 
                        color = MaterialTheme.colorScheme.primary, 
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
